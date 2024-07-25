package com.smartdocs.gpt.document.model;

import lombok.Data;

@Data
public class TrainDocumentRequest {
	public static final String DOCUMENT_FILE_CATEGORY="file";
	public static final String DOCUMENT_HTML_CATEGORY="html";
	public static final String DOCUMENT_ARTICLE_CATEGORY="article";
	public static final String DOCUMENT_QNA_CATEGORY="qna";
	
	public static final String DOCUMENT_SOURCE_FILE="file";
	public static final String DOCUMENT_SOURCE_URL="url";
	public static final String DOCUMENT_SOURCE_TEXT="text";
	
	private String siteId;
	private String documentId;
	private String documentName;
	private String documentCategory;
	private String url;
	private String content;
	private String question;
	private String answer;
	private String htmlLink;
	private String fileName;
	private String resourceId;
	

}
