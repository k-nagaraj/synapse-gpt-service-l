package com.smartdocs.gpt.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartdocs.gpt.document.model.TrainDocumentRequest;
import com.smartdocs.gpt.openai.model.ChatResponse;
import com.smartdocs.gpt.openai.model.EmbedingsResponse;
import com.smartdocs.gpt.openai.service.OpenAIService;
import com.smartdocs.gpt.service.TrainingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
	
	private final OpenAIService openAIService;
	private final TrainingService trainingService;

	@GetMapping("/message")
	public String getTrainingStatus() {
		return "message";
	}

	@PostMapping("/chat")
	public ChatResponse createChatCompletion(@RequestParam String prompt) {
		return openAIService.createChatCompletion(prompt);

	}

	@PostMapping("/embedings")
	public EmbedingsResponse createEmbeddings(@RequestParam String text) {

		EmbedingsResponse response = openAIService.createEmbeddings(text);
		response.getData().get(0).getEmbedding();
		return response;

	}

	@PostMapping("/train")
	public boolean trainDocument(@RequestBody TrainDocumentRequest trainDocumentRequest) {

		return trainingService.train(trainDocumentRequest);

	}
}
