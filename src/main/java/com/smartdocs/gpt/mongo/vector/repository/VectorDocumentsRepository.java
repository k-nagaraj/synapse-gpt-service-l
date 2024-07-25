package com.smartdocs.gpt.mongo.vector.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartdocs.gpt.mongo.vector.collection.VectorDocuments;

public interface VectorDocumentsRepository extends MongoRepository<VectorDocuments, String> {

	void deleteByDocumentId(String documentId);

	void deleteBySiteId(String siteId);

}
