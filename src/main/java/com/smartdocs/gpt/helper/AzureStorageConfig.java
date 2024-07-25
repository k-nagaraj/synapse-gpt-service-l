package com.smartdocs.gpt.helper;

import lombok.Data;

@Data
public class AzureStorageConfig {

	public static final String TYPE_AZURE_STORAGE ="azurestorage";
	
	private String type;
	private String accountName;
	private String accountKey;
	private String container;
	
	 
}
