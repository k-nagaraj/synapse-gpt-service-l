package com.smartdocs.gpt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartdocs.gpt.document.model.TrainDocumentRequest;
import com.smartdocs.gpt.service.TrainingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TrainController {
	
	private final TrainingService trainingService;

	@PostMapping("/train")
	public boolean trainDocument(@RequestBody TrainDocumentRequest trainDocumentRequest) {

		return trainingService.train(trainDocumentRequest);

	}
	
	@DeleteMapping("/deleteDocument")
	public ResponseEntity<String> deleteDocument(@RequestParam String documentId) {
		try {
			trainingService.deleteDocument(documentId);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), null, 500);
		}
		return new ResponseEntity<>(" ",null, 200);
	}

}
