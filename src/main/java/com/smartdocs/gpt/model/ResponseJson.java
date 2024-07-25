package com.smartdocs.gpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseJson {

	@JsonProperty("response")
	private String response;

	@JsonProperty("confidenceScore")
	private int confidenceScore;

}
