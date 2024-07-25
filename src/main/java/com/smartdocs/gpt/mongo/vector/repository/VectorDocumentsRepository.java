package com.smartdocs.gpt.mongo.vector.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.smartdocs.gpt.mongo.vector.collection.VectorDocuments;

public interface VectorDocumentsRepository extends MongoRepository<VectorDocuments, String> {

	void deleteByDocumentId(String documentId);
	
	@Transactional
    @Query("{ 'siteId' : ?0 }")  
    void deleteBySiteId(String siteId);
	
	
}
