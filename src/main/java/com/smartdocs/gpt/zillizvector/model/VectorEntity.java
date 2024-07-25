package com.smartdocs.gpt.zillizvector.model;

import java.util.List;

import lombok.Data;

@Data
public class VectorEntity {
	private String id;
	private String tenentId;
	private String siteId;
	private String documentId;
	private  int page;
	private List<Float> embedding;

}