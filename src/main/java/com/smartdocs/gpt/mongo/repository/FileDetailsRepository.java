package com.smartdocs.gpt.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.smartdocs.gpt.mongo.model.FileDetails;



@Repository
public interface FileDetailsRepository extends MongoRepository<FileDetails, String>{
	List<FileDetails> findByZillId(String zillId);
	
	List<FileDetails> findByDbId(String dbId);

	void deleteByDocumentId(String documentId);

}
