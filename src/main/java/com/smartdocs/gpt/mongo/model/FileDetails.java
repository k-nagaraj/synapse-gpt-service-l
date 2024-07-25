package com.smartdocs.gpt.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "GPTFileDetails")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileDetails {

	@Id
	private String id;
	private String siteId;
	private String documentType;
	private String documentId;
	private String zillId;
	private String source;
	private String dbId;

	public FileDetails(String siteId, String documentType, String documentId, String zillId, String source) {
		super();
		this.siteId = siteId;
		this.documentType = documentType;
		this.documentId = documentId;
		this.zillId = zillId;
		this.source = source;
	}
	
	public FileDetails(String siteId, String documentType, String documentId, String zillId, String source,String dbId) {
		this(siteId, documentType, documentId, zillId, source);
	    this.dbId=dbId;
	}


}
