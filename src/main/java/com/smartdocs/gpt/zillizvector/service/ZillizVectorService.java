package com.smartdocs.gpt.zillizvector.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smartdocs.gpt.zillizvector.model.VectorEntity;
import com.smartdocs.gpt.zillizvector.repository.VectorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZillizVectorService {

	private final VectorRepository vectorRepository;

	@Value("${zilliz.vector.collectionName}")
	private String collectionName;

	public List<String> insertRows(List<VectorEntity> vectorEntities) {
		List<?> insertIds = vectorRepository.insertRows(collectionName, vectorEntities);

		return Arrays
				.stream(insertIds.toString().substring(1, insertIds.toString().length() - 1).split(","))
				.map(String::trim).toList();
	}

	public List<VectorEntity>  search(String tenantId, String siteId, List<String> documentIds, List<List<Float>> queryVectorList) {

		List<String> outputFields = new ArrayList<>();
		outputFields.add("id");
		outputFields.add("page");
		outputFields.add("documentId");
		return vectorRepository.search(collectionName, tenantId, siteId, documentIds, queryVectorList, outputFields);
	}
	
	public List<String> query( String documentId){
		return vectorRepository.query(collectionName, documentId);
	}
	
	public String deleteRows(List<String> documentIds) {
		 vectorRepository.deleteRows(collectionName, documentIds);
		 return "Deleted";
	}

}
