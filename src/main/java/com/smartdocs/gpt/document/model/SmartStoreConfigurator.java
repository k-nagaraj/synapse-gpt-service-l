package com.smartdocs.gpt.document.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "SmartStore_Configurator")
@Data
@NoArgsConstructor
public class SmartStoreConfigurator {
    
    public static final String SMARTSTORE_ID = "1000";

    @Id
    private String id;
    private String serverURL;
    private String system;
    private String pVersion;
    private String contRep;
    private String compId;
    private String authId;
    private String expiration;
    private String secKey;

}