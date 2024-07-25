package com.smartdocs.gpt.service;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.smartdocs.gpt.helper.AzureStorageConfig;
import com.smartdocs.gpt.model.SystemConfig;
import com.smartdocs.gpt.mongo.repository.SystemConfigRepository;

import lombok.RequiredArgsConstructor;

@Service
@DependsOn({ "systemConfigRepository" })
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class AzureTenantBlobService {

	private BlobContainerClient blobContainerClient;
	
	
	@Autowired
	private void setSystemConfigRepository(SystemConfigRepository systemConfigRepository) {
		 
		SystemConfig config = systemConfigRepository.findById(SystemConfig.ID).orElse(new SystemConfig());
		if (config.getStorageConfig() != null) {
			try {
				BlobServiceClient blobServiceClient = getClient(config.getStorageConfig());
				this.blobContainerClient = blobServiceClient
						.getBlobContainerClient(config.getStorageConfig().getContainer());
			} catch (BlobStorageException ex) {
				System.out.println(ex.getMessage());
				// TODO: handle exception
			}
			
		}
	}


	private BlobServiceClient getClient(AzureStorageConfig storageConfig) {

		StorageSharedKeyCredential credential = new StorageSharedKeyCredential(storageConfig.getAccountName(),
				storageConfig.getAccountKey());

		/*
		 * From the Azure portal, get your Storage account blob service URL endpoint.
		 * The URL typically looks like this:
		 */
		String endpoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net",
				storageConfig.getAccountName());



		/*
		 * Create a BlobServiceClient object that wraps the service endpoint, credential
		 * and a request pipeline.
		 */
		return new BlobServiceClientBuilder().endpoint(endpoint).credential(credential).buildClient();

	}



	public byte[] getFile(String resourceId) {
		
			BlobClient blob = blobContainerClient.getBlobClient(resourceId);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			blob.download(outputStream);
			return outputStream.toByteArray();
	}


	

}
