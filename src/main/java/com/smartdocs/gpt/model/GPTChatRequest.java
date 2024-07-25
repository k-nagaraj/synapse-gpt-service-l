package com.smartdocs.gpt.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GPTChatRequest {
	
	private String message;

	private String siteId;

	private double temperature=0.9;

	private String persona="helpful assistant";
	private List<String> attributes=new ArrayList<>();

	private int outputLength=4000;

	private List<String> fileIds=new ArrayList<>();

	private String basePrompt="";
	
	private String source;
	
	

}
