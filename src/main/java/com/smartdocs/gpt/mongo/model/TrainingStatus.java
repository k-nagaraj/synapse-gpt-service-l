package com.smartdocs.gpt.mongo.model;

import java.time.ZonedDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "GPTTrainingStatus")
public class TrainingStatus {
public static final String TRAINING_SUCCESS="Success";
public static final String TRAINING_FAILED= "Failed";
public static final String UNSUPPORTED_FILE="UnsupportedFile";
	private String id;
	

	private String documentId;
	
	private String status;
	private String errorMessage;
	private ZonedDateTime date=ZonedDateTime.now();

	public TrainingStatus(String id, String documentId, String status) {
		super();
		this.id = id;
		this.documentId = documentId;
		this.status = status;
	}

	public TrainingStatus() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
		this.date = date;
	}

	

	
	
}
