package com.smartdocs.gpt.openai.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smartdocs.gpt.model.ResponseFormat;
import com.smartdocs.gpt.openai.model.ChatRequest;
import com.smartdocs.gpt.openai.model.ChatResponse;
import com.smartdocs.gpt.openai.model.EmbedingsRequest;
import com.smartdocs.gpt.openai.model.EmbedingsResponse;
import com.smartdocs.gpt.openai.model.Message;
import com.smartdocs.gpt.service.OpenAIConfigProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("openAIService")
@Slf4j
@RequiredArgsConstructor
public class OpenAIService {
	
	@Qualifier("openaiRestTemplate")
	private final RestTemplate restTemplate;

	private final OpenAIConfigProperties openAiConfigProperties;

	public ChatResponse createChatCompletion(String prompt) {
		ChatRequest request = new ChatRequest(openAiConfigProperties.getModelName(), prompt);
		return restTemplate.postForObject(openAiConfigProperties.getApiUrl() + "/chat/completions", request, ChatResponse.class);

	}

	public ChatResponse createChatCompletion(List<Message> messages, int maxTokens, double temperature) {
		ResponseFormat responseformat = new ResponseFormat();
		responseformat.setType("json_object");
		ChatRequest request = new ChatRequest(openAiConfigProperties.getModelName(), messages, maxTokens, temperature,responseformat);
		log.info(request.toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(request, headers);
		var response=restTemplate.exchange(openAiConfigProperties.getApiUrl() + "/chat/completions", HttpMethod.POST, requestEntity, ChatResponse.class);
		return response.getBody();

	}

	public EmbedingsResponse createEmbeddings(String text) {
		EmbedingsRequest request = new EmbedingsRequest(openAiConfigProperties.getEmbedingsModel(), text);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<EmbedingsRequest> requestEntity = new HttpEntity<>(request, headers);
		
		ResponseEntity<EmbedingsResponse> embedingsResponse=restTemplate.exchange((openAiConfigProperties.getApiUrl() + "/embeddings"), HttpMethod.POST,requestEntity, EmbedingsResponse.class);
		return embedingsResponse.getBody();

	}
	
	public List<Double> createEmbeddingInDouble(String text) {
		EmbedingsResponse response =createEmbeddings(text);

		List<Float> queryEmbeddings = response.getData().get(0).getEmbedding();
		
		List<Double> queryEmbeddingsDouble = new ArrayList<>();
		for (Float f : queryEmbeddings) {
			queryEmbeddingsDouble.add(f.doubleValue());
		}
		return queryEmbeddingsDouble;
		
	}

}
