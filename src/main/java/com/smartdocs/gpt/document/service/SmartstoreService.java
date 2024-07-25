package com.smartdocs.gpt.document.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartdocs.gpt.document.model.SmartStoreConfigurator;
import com.smartdocs.gpt.zillizvector.repository.SmartStoreConfiguratorRepository;

@Service
public class SmartstoreService {
	
	@Autowired
	private SmartStoreConfiguratorRepository smartStoreConfiguratorRepository;
	
	
	public SmartStoreConfigurator getSmartStoreDetails() {
	    Optional<SmartStoreConfigurator> findById = smartStoreConfiguratorRepository.findById(SmartStoreConfigurator.SMARTSTORE_ID);
	       return findById.orElse(new SmartStoreConfigurator());
	   }

}
