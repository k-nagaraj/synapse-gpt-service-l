package com.smartdocs.gpt.openai.model;

import java.util.List;

import lombok.Data;

@Data
public class GenerateUtteranceDto {
	
	private List<String> utterance;
	private int numberOfUtterance;

}
