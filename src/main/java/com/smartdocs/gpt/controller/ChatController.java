package com.smartdocs.gpt.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartdocs.gpt.model.GPTChatRequest;
import com.smartdocs.gpt.model.GPTChatResponse;
import com.smartdocs.gpt.service.ChatService;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatService chatService;

	@PostMapping("/chat")
	public GPTChatResponse chat(@RequestBody GPTChatRequest request) {
		return chatService.chat(request);

	}

}
