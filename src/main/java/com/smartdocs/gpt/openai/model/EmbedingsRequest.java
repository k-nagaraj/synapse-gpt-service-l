package com.smartdocs.gpt.openai.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmbedingsRequest {
	private String model;
	private String input;
	
	

}
