package com.smartdocs.gpt.mongo.vector.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.search.FieldSearchPath;
import com.smartdocs.gpt.helper.TenantContext;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.vectorSearch;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.search.SearchPath.fieldPath;
import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class AtlasVectorSearch {

    private final MongoClient mongoClient;

    public void search(List<Double> queryVector) {

        MongoDatabase database = mongoClient.getDatabase(TenantContext.getTenantId());
        MongoCollection<Document> collection = database.getCollection("VectorDocuments");

        String indexName = "vector_index";
        FieldSearchPath fieldSearchPath = fieldPath("embeddings");
        int numCandidates = 200;
        int limit = 10;

        List<Bson> pipeline = asList(
                vectorSearch(
                        fieldSearchPath,
                        queryVector,
                        indexName,
                        numCandidates,
                        limit
                ),
                project(
                        fields(exclude("_id"), include("documentName"), include("documentContent"), include("embeddings"),
                                include("documentId"))));


        collection.aggregate(pipeline)
                .forEach(doc -> System.out.println(doc.toJson()));
    }

}

