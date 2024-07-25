package com.smartdocs.gpt.mongo.vector.repository;

import com.smartdocs.gpt.mongo.vector.collection.VectorDocuments;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VectorDocumentsRepository extends MongoRepository<VectorDocuments, String> {

	void deleteByDocumentId(String documentId);
}
