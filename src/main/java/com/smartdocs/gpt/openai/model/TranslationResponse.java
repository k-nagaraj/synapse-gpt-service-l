package com.smartdocs.gpt.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TranslationResponse {

	@JsonProperty("detected_language")
	private String detected_Language;
	
	private String original_text;
	
	private String translated_text;



}
