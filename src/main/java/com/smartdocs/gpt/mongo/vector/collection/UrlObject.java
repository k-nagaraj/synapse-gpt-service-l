package com.smartdocs.gpt.mongo.vector.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Data;

@Data
@Document("Url-Object")
public class UrlObject {
	@Id
	private String id;
	
	private String content;

}
