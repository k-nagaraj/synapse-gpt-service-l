package com.smartdocs.gpt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartdocs.gpt.model.SourceObject;
import com.smartdocs.gpt.mongo.model.FileDetails;
import com.smartdocs.gpt.mongo.repository.FileDetailsRepository;
import com.smartdocs.gpt.mongo.vector.collection.VectorDocuments;
import com.smartdocs.gpt.zillizvector.model.VectorEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {
	
	private final FileDetailsRepository fileDetailsRepository;

	public List<SourceObject> getSourseObjectList(List<VectorEntity> vectorEntityList) {
		List<SourceObject> sourceObjectList = new ArrayList<>();
		for (VectorEntity vectorEntity : vectorEntityList) {
			List<FileDetails> fileDeails = fileDetailsRepository.findByZillId(vectorEntity.getId());
			for (FileDetails fileDetails : fileDeails) {
				SourceObject sourceObject = new SourceObject();
				sourceObject.setDocumentId(fileDetails.getDocumentId());
				sourceObject.setDocumentType(fileDetails.getDocumentType());
				sourceObject.setSource(fileDetails.getSource());
				sourceObject.setPage_no("" + vectorEntity.getPage());
				sourceObjectList.add(sourceObject);

			}

		}

		return sourceObjectList;
	}
	
	public List<SourceObject> sourseObjectList(List<VectorDocuments> documents) {
		List<SourceObject> sourceObjectList = new ArrayList<>();
		for (VectorDocuments vectorEntity : documents) {
			List<FileDetails> fileDeails = fileDetailsRepository.findByDbId(vectorEntity.getId());
			for (FileDetails fileDetails : fileDeails) {
				SourceObject sourceObject = new SourceObject();
				sourceObject.setDocumentId(fileDetails.getDocumentId());
				sourceObject.setDocumentType(fileDetails.getDocumentType());
				sourceObject.setSource(fileDetails.getSource());
				sourceObject.setPage_no("" + vectorEntity.getPage());
				sourceObjectList.add(sourceObject);

			}

		}

		return sourceObjectList;
	}
	
	public List<SourceObject> getSourceListFromDocument(List<VectorDocuments> documents) {
		List<SourceObject> sourceObjectList = new ArrayList<>();
		for (VectorDocuments vectorEntity : documents) {
			
				SourceObject sourceObject = new SourceObject();
				sourceObject.setDocumentId(vectorEntity.getDocumentId());
				sourceObject.setDocumentType("");
				sourceObject.setSource(vectorEntity.getDocumentContent());
				sourceObject.setPage_no("" + vectorEntity.getPage());
				sourceObjectList.add(sourceObject);

			

		}

		return sourceObjectList;
	}

}
