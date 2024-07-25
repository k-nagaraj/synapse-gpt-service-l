package com.smartdocs.gpt.zillizvector.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartdocs.gpt.document.model.SmartStoreConfigurator;

public interface SmartStoreConfiguratorRepository extends MongoRepository<SmartStoreConfigurator, String> {

}