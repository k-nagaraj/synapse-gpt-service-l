package com.smartdocs.gpt.openai.model;

import java.util.ArrayList;
import java.util.List;

import com.smartdocs.gpt.model.ResponseFormat;

import lombok.Data;

@Data
public class ChatRequestJson {
	
	private String model;
	private List<Message> messages;
	private int max_tokens;
	private double temperature;
	private ResponseFormat response_format;

	public ChatRequestJson(String model, String prompt) {
		this.model = model;

		this.messages = new ArrayList<>();
		this.messages.add(new Message("user", prompt));
	}

	public ChatRequestJson(String model, List<Message> messages, int max_tokens, double temperature,ResponseFormat responseformat) {
		this.model = model;
		this.messages = messages;
		this.max_tokens = max_tokens;
		this.temperature = temperature;
		this.response_format=responseformat;
	}

	@Override
	public String toString() {
		return "ChatRequest [model=" + model + ", messages=" + messages + ", max_tokens=" + max_tokens
				+ ", temperature=" + temperature + "response_format="+ "]";
	}

}
