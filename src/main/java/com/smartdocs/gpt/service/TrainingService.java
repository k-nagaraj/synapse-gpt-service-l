package com.smartdocs.gpt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartdocs.gpt.document.model.Document;
import com.smartdocs.gpt.document.model.TextSegment;
import com.smartdocs.gpt.document.model.TrainDocumentRequest;
import com.smartdocs.gpt.document.service.DocumentService;
import com.smartdocs.gpt.helper.TenantContext;
import com.smartdocs.gpt.mongo.model.FileDetails;
import com.smartdocs.gpt.mongo.model.TrainingStatus;
import com.smartdocs.gpt.mongo.repository.FileDetailsRepository;
import com.smartdocs.gpt.mongo.repository.TrainingStatusRepository;
import com.smartdocs.gpt.openai.model.EmbedingsResponse;
import com.smartdocs.gpt.openai.service.OpenAIService;
import com.smartdocs.gpt.zillizvector.model.VectorEntity;
import com.smartdocs.gpt.zillizvector.service.ZillizVectorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingService {

	private final DocumentService documentService;

	private final OpenAIService openAIService;
	 
	private final ZillizVectorService zillizVectorService;
	 
	private final TrainingStatusRepository trainingStatusRepository;
	 
	private final FileDetailsRepository fileDetailsRepository;


	public boolean train(TrainDocumentRequest trainDocumentRequest) {

		TrainingStatus trainingStatus = new TrainingStatus();
		trainingStatus.setDocumentId(trainDocumentRequest.getDocumentId());
		trainingStatus.setStatus("In-Progress");
		trainingStatus = trainingStatusRepository.save(trainingStatus);

		String tenantId = TenantContext.getTenantId();

		try {
			Map<Integer, Document> documents = documentService.processDocument(trainDocumentRequest);
			List<VectorEntity> documentEmbeddings = new ArrayList<>();
			List<String> finalChunkList = new ArrayList<>();

			for (Map.Entry<Integer, Document> itr : documents.entrySet()) {

				int page = itr.getKey();
				Document document = itr.getValue();

				List<TextSegment> segments = documentService.split(document);

				for (TextSegment textSegment : segments) {
					EmbedingsResponse response = openAIService.createEmbeddings(textSegment.text());

					List<Float> embeddings = response.getData().get(0).getEmbedding();
					VectorEntity documentEmbedding = new VectorEntity();
					documentEmbedding.setTenentId(tenantId);
					documentEmbedding.setSiteId(trainDocumentRequest.getSiteId());
					documentEmbedding.setDocumentId(trainDocumentRequest.getDocumentId());
					documentEmbedding.setPage(page);
					documentEmbedding.setEmbedding(embeddings);
					documentEmbeddings.add(documentEmbedding);
					finalChunkList.add(textSegment.text());
				}

			}
			List<String> idsList = zillizVectorService.insertRows(documentEmbeddings);

			List<FileDetails> fileDetailList = new ArrayList<>();
			for (int a = 0; a < finalChunkList.size() && a < idsList.size(); a++) {

				FileDetails fileDetails = new FileDetails(trainDocumentRequest.getSiteId(),
						trainDocumentRequest.getDocumentCategory(), trainDocumentRequest.getDocumentId(),
						idsList.get(a), finalChunkList.get(a));

				fileDetailList.add(fileDetails);
			}
			fileDetailsRepository.saveAll(fileDetailList);
			trainingStatus.setStatus("Completed");

		} catch (Exception e) {

			trainingStatus.setErrorMessage(e.getMessage());
			log.info("Some Error occurred while training");
			trainingStatus.setStatus("Some error occurred");
			e.printStackTrace();
		}

		trainingStatusRepository.save(trainingStatus);

		return true;
	}

	public Optional<String> getFileExtension(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	
	
	public String deleteDocument(String documentId) {
		List<String> ids= zillizVectorService.query( documentId);
		if(ids== null) throw new RuntimeException("Document not found");
		return zillizVectorService.deleteRows( ids);
		
	}
	

}
