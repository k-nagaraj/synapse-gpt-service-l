package com.smartdocs.gpt.helper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PhraseResponse {
	
	@JsonProperty("paraphrases")
    private List<String> paraphrases;

}
