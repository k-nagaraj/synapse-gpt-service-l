package com.smartdocs.gpt.model;

import java.util.List;

import lombok.Data;

@Data
public class GPTChatResponse {

	
	private String response;
	
	private List<SourceObject> sources;
	

	
	private String language;
	private String en_response;
	private String en_query;
	private boolean createTicket;


}
