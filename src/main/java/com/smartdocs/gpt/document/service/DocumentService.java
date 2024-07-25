package com.smartdocs.gpt.document.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.smartdocs.gpt.document.model.Document;
import com.smartdocs.gpt.document.model.TextSegment;
import com.smartdocs.gpt.document.model.TrainDocumentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

	private final DocumentGetService documentGetService;

	public Map<Integer, Document> processDocument(TrainDocumentRequest trainDocumentRequest) throws IOException {
		Map<Integer, Document> documents = new HashMap<>();

		String documentCategory = trainDocumentRequest.getDocumentCategory();

		if (TrainDocumentRequest.DOCUMENT_FILE_CATEGORY.equalsIgnoreCase(documentCategory)) {
			String fileName = trainDocumentRequest.getFileName();
			byte[] bytes = documentGetService.getBytesByDocId(trainDocumentRequest.getResourceId(), fileName);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

			if (StringUtils.isNotBlank(fileName)) {
				Optional<String> fileExtensionOpt = getFileExtension(fileName);

				if (fileExtensionOpt.isPresent()) {
					String fileExtension = fileExtensionOpt.get();

					documents = switch (fileExtension.toLowerCase()) {
					case "doc", "docx", "xls", "xlsx", "ppt", "pptx" ->
						DocumentParser.msOfficeDocumentParser(inputStream);
					case "txt" -> DocumentParser.textDocumentparse(inputStream);
					case "pdf" -> DocumentParser.pdfDocumentParse(inputStream);
					default -> documents;
					};
				}
			}
		} else if (TrainDocumentRequest.DOCUMENT_ARTICLE_CATEGORY.equalsIgnoreCase(documentCategory)) {
			documents = DocumentParser.articleDocumentParse(trainDocumentRequest.getContent());
		} else if (TrainDocumentRequest.DOCUMENT_QNA_CATEGORY.equalsIgnoreCase(documentCategory)) {
			documents = DocumentParser.qnaDocumentParse(trainDocumentRequest.getQuestion(),
					trainDocumentRequest.getAnswer());
		} else if (TrainDocumentRequest.DOCUMENT_HTML_CATEGORY.equalsIgnoreCase(documentCategory)) {
			documents = DocumentParser.htmlDocumentParse(trainDocumentRequest.getHtmlLink());
		}

		return documents;
	}

	public List<TextSegment> split(Document document) {
		return new DocumentSplitter(7061, 0).charsplit(document);

	}

	private Optional<String> getFileExtension(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

}
