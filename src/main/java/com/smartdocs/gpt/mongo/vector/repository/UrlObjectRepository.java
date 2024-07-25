package com.smartdocs.gpt.mongo.vector.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.smartdocs.gpt.mongo.vector.collection.UrlObject;

@Repository
public interface UrlObjectRepository extends MongoRepository<UrlObject, String>{

}
