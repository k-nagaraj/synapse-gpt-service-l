package com.smartdocs.gpt.document.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.smartdocs.gpt.document.model.Document;
import com.smartdocs.gpt.document.util.HtmlToPlainText;

public class DocumentParser {

	public static Map<Integer, Document> msOfficeDocumentParser(InputStream inputStream) {
		Map<Integer, Document> content = new HashMap<Integer, Document>();
		try (POITextExtractor extractor = ExtractorFactory.createExtractor(inputStream)) {
			String text = extractor.getText();
			content.put(0, Document.from(preprocessText(text)));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return content;
	}

	public static Map<Integer, Document> textDocumentparse(InputStream inputStream) {
		Map<Integer, Document> content = new HashMap<Integer, Document>();

		try {

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();

			String text = new String(buffer.toByteArray(), "UTF_8");

			content.put(0, Document.from(preprocessText(text)));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return content;
	}

	public static Map<Integer, Document> pdfDocumentParse(InputStream inputStream) {
		Map<Integer, Document> content = new HashMap<>();

		try {

			PdfReader pdfReader = new PdfReader(inputStream);
			int numberOfPages = pdfReader.getNumberOfPages();
			for (int page = 1; page <= numberOfPages; page++) {
				String pageText = PdfTextExtractor.getTextFromPage(pdfReader, page);
				if (pageText != null && !pageText.isEmpty()) {
					content.put(page, Document.from(preprocessText(pageText)));
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return content;
	}

	public static Map<Integer, Document> qnaDocumentParse(String question, String answer) {
		Map<Integer, Document> content = new HashMap<Integer, Document>();

		try {

			StringBuilder sb = new StringBuilder(
					"this is the pair of question and answer which is the source in this case");
			sb.append("'''\nQuestion: " + question + "\n\n");
			sb.append("Answer: " + answer + "'''");

			content.put(0, Document.from(preprocessText(sb.toString())));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return content;
	}

	public static Map<Integer, Document> htmlDocumentParse(String htmlLink) {
		Map<Integer, Document> content = new HashMap<Integer, Document>();

		try {

			String textContnt = HtmlToPlainText.convert(htmlLink, null);
			content.put(0, Document.from(preprocessText(textContnt)));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return content;
	}

	public static Map<Integer, Document> articleDocumentParse(String data) {
		Map<Integer, Document> content = new HashMap<Integer, Document>();

		try {

			content.put(0, Document.from(preprocessText(data)));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return content;
	}

	// function to clean string
	private static String preprocessText(String inputText) {
		Pattern nonAlphanumericPattern = Pattern.compile("[^a-zA-Z0-9\\s]");
		Pattern extraSpacesPattern = Pattern.compile("\\s{2,}");
		Pattern newlinePattern = Pattern.compile("\\r?\\n");

		Matcher matcher = nonAlphanumericPattern.matcher(inputText);
		String cleanText = matcher.replaceAll(" ");
		matcher = newlinePattern.matcher(cleanText);
		cleanText = matcher.replaceAll(" ");

		matcher = extraSpacesPattern.matcher(cleanText);
		cleanText = matcher.replaceAll(" ");

		return cleanText.trim();
	}

	// function to get chunks
	public static List<String> getChunks(String input) {
		List<String> dividedStrings = new ArrayList<>();
		int maxLength = 7061;

		int length = input.length();
		int startIndex = 0;

		while (startIndex < length) {
			int endIndex = Math.min(startIndex + maxLength, length);
			String substring = input.substring(startIndex, endIndex);
			dividedStrings.add(substring);
			startIndex = endIndex;
		}

		return dividedStrings;
	}
}