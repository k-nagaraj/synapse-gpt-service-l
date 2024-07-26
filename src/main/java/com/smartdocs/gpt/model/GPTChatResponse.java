package com.smartdocs.gpt.model;

import java.util.List;

import lombok.Data;

@Data
public class GPTChatResponse {

	
	private String response;
	
	private List<SourceObject> sources;
	

	



}
