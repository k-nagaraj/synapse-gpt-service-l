package com.smartdocs.gpt.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.smartdocs.gpt.model.GptConfig;


@Repository
public interface GptConfigRepository extends MongoRepository<GptConfig, String>{

}
