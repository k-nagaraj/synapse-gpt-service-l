package com.smartdocs.gpt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "GptConfig")
@NoArgsConstructor
@AllArgsConstructor
public class GptConfig {
	
	public static final String ID = "1000";
	
	public  static final String KEY_DEFAULT = "Default";
	public  static final String KEY_CUSTOM = "Custom";

	@Id
	@JsonIgnore
	private String id = ID;
	private String apiKey;
	private String modelName;
	private String gptProvider;
	private String keyType;  

}

