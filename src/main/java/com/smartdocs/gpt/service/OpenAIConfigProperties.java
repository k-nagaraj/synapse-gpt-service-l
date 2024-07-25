package com.smartdocs.gpt.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.smartdocs.gpt.model.GptConfig;
import com.smartdocs.gpt.mongo.repository.GptConfigRepository;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "openai")
@Component
@DependsOn({ "openAIService" })
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OpenAIConfigProperties {

	private String apiKey;
	private String embedingsModel;
	private String apiUrl;
	private String modelName;
	private String gptProvider;

	private final GptConfigRepository gptConfigRepository;

	public OpenAIConfigProperties(GptConfigRepository gptConfigRepository) {
		this.gptConfigRepository = gptConfigRepository;
		buildGptConfig();

	}

	private void buildGptConfig() {
		Optional<GptConfig> existCfg = gptConfigRepository.findById(GptConfig.ID);
		if (existCfg.isPresent()) {
			setApiKey(existCfg.get().getApiKey());
			setModelName(existCfg.get().getModelName());
			setGptProvider(existCfg.get().getGptProvider());
			setApiKey(getOpenaiApiKey());
		}

	}

	private String getOpenaiApiKey() {
		Optional<GptConfig> existCfg = gptConfigRepository.findById(GptConfig.ID);
		if (existCfg.isPresent() && GptConfig.KEY_CUSTOM.equals(existCfg.get().getKeyType())
				&& StringUtils.isNotBlank(existCfg.get().getApiKey())) {
			return existCfg.get().getApiKey();
		}
		return apiKey;
	}

}
