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

import com.smartdocs.gpt.document.model.TrainDocumentRequest;
import com.smartdocs.gpt.model.GPTChatRequest;
import com.smartdocs.gpt.model.GPTChatResponse;
import com.smartdocs.gpt.mongo.vector.service.AltasDocumentService;

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
	
	@PostMapping("/trainUrl/{siteId}")
	public ResponseEntity<Boolean> trainOnUrl(@RequestBody List<String> urls, @PathVariable(value="siteId") String siteId) {
		altasDocumentService.trainOnUrl(urls,siteId);
		return ResponseEntity.ok(true);
	}

}
