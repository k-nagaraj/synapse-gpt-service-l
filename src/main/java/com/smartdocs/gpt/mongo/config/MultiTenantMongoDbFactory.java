package com.smartdocs.gpt.mongo.config;

import java.util.Objects;

import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.smartdocs.gpt.helper.TenantContext;

public class MultiTenantMongoDbFactory extends SimpleMongoClientDatabaseFactory {
    private final String masterDB;

    
    public MultiTenantMongoDbFactory(MongoClient mongoClient, String masterDB) {
        super(mongoClient, masterDB);
        this.masterDB = masterDB;
    }
    @Override
    public MongoDatabase getMongoDatabase() {
        return getMongoClient().getDatabase(getTenantDatabase());
    }

    protected String getTenantDatabase() {
        String tenantId = TenantContext.getTenantId();
        if (Objects.nonNull(tenantId)) {
            return tenantId;
        } else {
            return masterDB;
        }
    }
   
}
