package com.smartdocs.gpt.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartdocs.gpt.model.SystemConfig;


public interface SystemConfigRepository extends MongoRepository<SystemConfig, String>{

}
