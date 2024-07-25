package com.smartdocs.gpt.zillizvector.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;

@Configuration
public class ZillizVectorConfig {

	@Value("${zilliz.vector.server.host}")
	private String vectorServerHost;

	@Value("${zilliz.vector.server.key}")
	private String vectorServerKey;

	@Bean
	public MilvusClient milvusClient() {
		try {

			return new MilvusServiceClient(
					ConnectParam.newBuilder().withUri(vectorServerHost).withToken(vectorServerKey).build());

		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();
			throw new IllegalStateException("Unable to connect to Zilliz Vector server", e);
		}

	}
}
