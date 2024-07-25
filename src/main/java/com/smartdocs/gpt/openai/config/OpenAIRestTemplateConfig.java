package com.smartdocs.gpt.openai.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.smartdocs.gpt.service.OpenAIConfigProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OpenAIRestTemplateConfig {

	
	private final OpenAIConfigProperties gptConfigProperties;

	@Bean
	@Qualifier("openaiRestTemplate")
	RestTemplate openaiRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add("Authorization", "Bearer " + gptConfigProperties.getApiKey() );
			return execution.execute(request, body);
		});
		return restTemplate;
	}
	

	
}
