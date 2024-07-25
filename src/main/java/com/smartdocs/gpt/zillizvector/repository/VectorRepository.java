package com.smartdocs.gpt.zillizvector.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.smartdocs.gpt.zillizvector.model.VectorEntity;

import io.milvus.client.MilvusClient;
import io.milvus.exception.ParamException;
import io.milvus.grpc.DataType;
import io.milvus.grpc.DescribeCollectionResponse;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.DescribeCollectionParam;
import io.milvus.param.collection.DropCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.FlushParam;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.highlevel.dml.DeleteIdsParam;
import io.milvus.param.highlevel.dml.InsertRowsParam;
import io.milvus.param.highlevel.dml.QuerySimpleParam;
import io.milvus.param.highlevel.dml.SearchSimpleParam;
import io.milvus.param.highlevel.dml.response.DeleteResponse;
import io.milvus.param.highlevel.dml.response.InsertResponse;
import io.milvus.param.highlevel.dml.response.QueryResponse;
import io.milvus.param.highlevel.dml.response.SearchResponse;
import io.milvus.param.index.CreateIndexParam;
import io.milvus.response.QueryResultsWrapper;

import io.milvus.response.QueryResultsWrapper.RowRecord;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class VectorRepository {
	

	
	
	private final MilvusClient milvusClient;

	public boolean createCollection(String collectionName, int dimension) {
		boolean isCollectionCreated = false;
		try {
			log.info("Creating collection: {}", collectionName);
			// Check if the collection exists
			R<DescribeCollectionResponse> responseR = milvusClient.describeCollection(
					DescribeCollectionParam.newBuilder().withCollectionName(collectionName).build());

			if (responseR.getData() != null) {
				milvusClient
						.dropCollection(DropCollectionParam.newBuilder().withCollectionName(collectionName).build());
			}

			FieldType id = FieldType.newBuilder().withName("id").withDataType(DataType.Int64).withPrimaryKey(true)
					.withAutoID(true).build();

			FieldType tenantIdField = FieldType.newBuilder().withName("tenantId").withDataType(DataType.VarChar)
					.withMaxLength(50).build();

			FieldType siteIdField = FieldType.newBuilder().withName("siteId").withDataType(DataType.VarChar)
					.withMaxLength(50).build();

			FieldType documentIdField = FieldType.newBuilder().withName("documentId").withDataType(DataType.VarChar)
					.withMaxLength(50).build();

			FieldType page = FieldType.newBuilder().withName("page").withDataType(DataType.Int64).build();

			FieldType embedding = FieldType.newBuilder().withName("embedding").withDataType(DataType.FloatVector)
					.withDimension(dimension).build();
			CreateCollectionParam createCollectionParam = CreateCollectionParam.newBuilder()
					.withCollectionName(collectionName).withDescription("my first collection").withShardsNum(2)
					.addFieldType(id).addFieldType(tenantIdField).addFieldType(siteIdField)
					.addFieldType(documentIdField).addFieldType(page).addFieldType(embedding).build();
			log.info("Creating example collection: " + collectionName);
			log.info("Schema: " + createCollectionParam);
			milvusClient.createCollection(createCollectionParam);
			log.info("Success!");

			log.info("Collection created successfully: {}", collectionName);

		} catch (ParamException e) {
			log.error("Create collection failed: {}", e.getMessage());
			e.printStackTrace();
			throw new IllegalStateException("Unable to create collection in Zilliz Vector", e);
		}
		return isCollectionCreated;
	}
	
	public void createIndex(String collectionName) {
		try {
			log.info("Creating index for collection: {}", collectionName);
			
			 milvusClient.createIndex(
		                CreateIndexParam.newBuilder()
		                        .withIndexName(collectionName+"_embedding")
		                        .withCollectionName(collectionName)
		                        .withFieldName("embedding")
		                        .withIndexType(IndexType.AUTOINDEX)
		                        .withMetricType(MetricType.L2)
		                        .withSyncMode(Boolean.TRUE)
		                        .withSyncWaitingInterval(500L)
		                        .withSyncWaitingTimeout(30L)
		                        .build());
			log.info("Index created successfully for collection: {}", collectionName);
		} catch (ParamException e) {
			log.error("Create index failed: {}", e.getMessage());
			e.printStackTrace();
			throw new IllegalStateException("Unable to create index in Zilliz Vector", e);
		}
	}
	
	public void loadCollection(String collectionName) {
		try {
			log.info("Loading collection: {}", collectionName);
			milvusClient.loadCollection(LoadCollectionParam.newBuilder()
	                .withCollectionName(collectionName)
	                .withSyncLoad(true)
	                .withSyncLoadWaitingInterval(500L)
	                .withSyncLoadWaitingTimeout(100L)
	                .build());			log.info("Collection loaded successfully: {}", collectionName);
		} catch (ParamException e) {
			log.error("Load collection failed: {}", e.getMessage());
			e.printStackTrace();
			throw new IllegalStateException("Unable to load collection in Zilliz Vector", e);
		}
	}
	
	public void flushCollection(String collectionName) {
		try {
			log.info("Flushing collection: {}", collectionName);
	        milvusClient.flush(FlushParam.newBuilder()
	                .withCollectionNames(Collections.singletonList(collectionName))
	                .withSyncFlush(true)
	                .withSyncFlushWaitingInterval(50L)
	                .withSyncFlushWaitingTimeout(30L)
	                .build());
			log.info("Collection flushed successfully: {}", collectionName);
		} catch (ParamException e) {
			log.error("Flush collection failed: {}", e.getMessage());
			e.printStackTrace();
			throw new IllegalStateException("Unable to flush collection in Zilliz Vector", e);
		}
	}
   
	public List<?> insertRows(String collectionName, List<VectorEntity> vectorEntities) {

		List<?> insertIds = null;

		List<JSONObject> rowsData = new ArrayList<>();
		for (VectorEntity vectorEntity : vectorEntities) {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("tenantId", vectorEntity.getTenentId());
			jsonObject.put("siteId", vectorEntity.getSiteId());
			jsonObject.put("documentId", vectorEntity.getDocumentId());
			jsonObject.put("page", Long.valueOf(vectorEntity.getPage()));
			jsonObject.put("embedding", vectorEntity.getEmbedding());
			rowsData.add(jsonObject);

		}
		try {
			log.info("Inserting vectors into collection: {}", collectionName);
			
			InsertRowsParam insertRowsParam = InsertRowsParam.newBuilder().withCollectionName(collectionName)
					.withRows(rowsData).build();

			R<InsertResponse> response = milvusClient.insert(insertRowsParam);


			if (response.getException() != null) {
				log.error("Insert failed: {}", response.getMessage());
				throw new IllegalStateException("Unable to insert vectors into Zilliz Vector");

			}
			insertIds = response.getData().getInsertIds();
			log.info("Vectors inserted successfully into collection: {}", collectionName);


		} catch (ParamException e) {
			log.error("Insert failed: {}", e.getMessage());
			e.printStackTrace();
			throw new IllegalStateException("Unable to insert vectors into Zilliz Vector", e);
		}
		return insertIds;
	}

	public List<VectorEntity> search(String collectionName, String tenantId, String siteId, List<String> documentIds,
			List<List<Float>> queryVectorList, List<String> outputFields) {

		List<VectorEntity> vectorEntities = new ArrayList<>();

		log.info("Searching vectors from collection: {}", collectionName);
		SearchSimpleParam searchSimpleParam = null;

		if (documentIds != null && !documentIds.isEmpty()) {

			searchSimpleParam = SearchSimpleParam.newBuilder().withCollectionName(collectionName)
					.withVectors(queryVectorList).withOutputFields(outputFields).withFilter("tenantId =='" + tenantId
							+ "' and siteId == '" + siteId + "' and documentId in " + formFileIdList(documentIds))
					.withOffset(0L).withLimit(3L).build();
		} else {

			searchSimpleParam = SearchSimpleParam.newBuilder().withCollectionName(collectionName)
					.withVectors(queryVectorList).withOutputFields(outputFields)
					.withFilter("tenantId == '" + tenantId + "' and siteId == '" + siteId + "'").withOffset(0L)
					.withLimit(3L).build();
		}

		R<SearchResponse> searchRes = milvusClient.search(searchSimpleParam);
		if (searchRes.getException() != null) {

			log.error("Search failed: {}", searchRes.getMessage());
			throw new IllegalStateException("Unable to search vectors from Zilliz Vector");
		} else {
			log.info("Search vectors successfully from collection: {}", collectionName);
			log.info("Search result: {}", searchRes.getData().toString());

			for (QueryResultsWrapper.RowRecord rowRecord : searchRes.getData().getRowRecords()) {
				VectorEntity vectorEntity = new VectorEntity();
				vectorEntity.setId(rowRecord.get("id").toString());
				vectorEntity.setDocumentId(rowRecord.get("documentId").toString());
				vectorEntity.setPage(Integer.valueOf(rowRecord.get("page").toString()));
				vectorEntity.setSiteId(siteId);
				vectorEntity.setTenentId(tenantId);

				vectorEntities.add(vectorEntity);
			}

		}
		return vectorEntities;
	}

	public String formFileIdList(List<String> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int a = 0; a < list.size(); a++) {
			if (a == list.size() - 1) {
				sb.append("'" + list.get(a) + "'");
			} else {
				sb.append("'" + list.get(a) + "'" + ",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public List<String> query(String collectionName, String documentId){
		List<String> outputFields = new ArrayList<>();
		outputFields.add("id");
		QuerySimpleParam querySimpleParam = QuerySimpleParam.newBuilder().withCollectionName(collectionName)
				.withFilter("documentId == '" + documentId + "'").withOutputFields(outputFields).withLimit(0L)
				.build();
		R<QueryResponse> queryRes = milvusClient.query(querySimpleParam);
		if (queryRes.getException() != null) {

			return null;
		}
		List<RowRecord> queryResults = new ArrayList<>();
		for (QueryResultsWrapper.RowRecord rowRecord : queryRes.getData().getRowRecords()) {
			queryResults.add(rowRecord);
		}
		List<String> ids = extractIds(queryResults.toString());
		return ids;
	}
	
	public List<String> extractIds(String input) {
		List<String> ids = new ArrayList<>();

		// Remove the outer brackets and split by '], ['
		String trimmedInput = input.substring(1, input.length() - 1);
		String[] parts = trimmedInput.split("\\], \\[");

		for (String part : parts) {
			// Extract the ID from each part
			String id = part.replaceAll("[\\[\\]id:]", "").trim();
			ids.add(id);
		}

		return ids;
	}
	
	public void deleteRows(String collectionName, List<String> ids) {
		DeleteIdsParam deleteParam = DeleteIdsParam.newBuilder().withCollectionName(collectionName).withPrimaryIds(ids)
				.build();
		R<DeleteResponse> deleteRes = milvusClient.delete(deleteParam);
		
	}

}
