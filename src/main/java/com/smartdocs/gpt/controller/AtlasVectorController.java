package com.smartdocs.gpt.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.smartdocs.gpt.document.model.TrainDocumentRequest;
import com.smartdocs.gpt.helper.PhraseResponse;
import com.smartdocs.gpt.model.GPTChatRequest;
import com.smartdocs.gpt.model.GPTChatResponse;
import com.smartdocs.gpt.mongo.vector.service.AltasDocumentService;
import com.smartdocs.gpt.openai.model.GenerateUtteranceDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/atlas-vector")
public class AtlasVectorController {

	private final AltasDocumentService altasDocumentService;

	@PostMapping("/train")
	public boolean trainDocument(@RequestBody TrainDocumentRequest trainDocumentRequest) {
		return altasDocumentService.train(trainDocumentRequest);

	}

	@PostMapping("/chat")
	public GPTChatResponse chat(@RequestBody GPTChatRequest request) throws IOException, InterruptedException {
		return altasDocumentService.chat(request);

	}

	@DeleteMapping("/deleteDocument")
	public void deleteDocument(@RequestParam String documentId) {
		try {
			altasDocumentService.deleteDocument(documentId);
		} catch (Exception e) {
			e.getMessage();
		}

	}
	
	@DeleteMapping("/deleteBot/{botId}")
	public void deleteBot(@PathVariable(value = "botId")String siteId ) {
		altasDocumentService.deleteBot(siteId);
	}
	
	@PostMapping("/trainUrl/{siteId}")
	public ResponseEntity<Boolean> trainOnUrl(@RequestBody List<String> urls, @PathVariable(value="siteId") String siteId) {
		altasDocumentService.trainOnUrl(urls,siteId);
		return ResponseEntity.ok(true);
	}
	
	
	@PostMapping("/train/bot")
	public boolean trainBot(@RequestBody TrainDocumentRequest trainDocumentRequest) {
		return altasDocumentService.trainBotDocuments(trainDocumentRequest);

	}
	
	@PostMapping("/generate/utterances")
	public PhraseResponse generateUtterance(@RequestBody GenerateUtteranceDto generateUtteranceDto) throws JsonMappingException, JsonProcessingException{
		return altasDocumentService.generateUtterance(generateUtteranceDto);
	}
	
	
	

}
