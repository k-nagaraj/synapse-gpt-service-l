package com.smartdocs.gpt.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartdocs.gpt.helper.AzureStorageConfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("SystemConfig")
@AllArgsConstructor
public class SystemConfig {
	
	public static final String ID="1000";
	public static final String STATUS_ACTIVE="ACTIVE";

	@Id
	@JsonIgnore
	private String id;
	
	private String customerId;
	
	private AzureStorageConfig storageConfig;
	
	private String sso;
	private String status;
	private String environment;
	
	
	public SystemConfig() {
		this.id=SystemConfig.ID;
	}

}
