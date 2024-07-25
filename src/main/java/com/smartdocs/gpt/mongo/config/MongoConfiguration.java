package com.smartdocs.gpt.mongo.config;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {
	@Value("${spring.data.mongodb.uri}")
	String connectionString;
	
	@Value("${tenant.default}")
	String defaultTenant;

	private MongoClient mongoClient;

	@Override
	@Primary
	@Bean
	public MongoDatabaseFactory mongoDbFactory() {
		return new MultiTenantMongoDbFactory(mongoClient(), defaultTenant);
	}

	@Override
	@Bean
	public MongoClient mongoClient() {
		try {

			ConnectionString connectionStringg = new ConnectionString(connectionString);

			final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
					.applyConnectionString(connectionStringg).build();
			mongoClient = MongoClients.create(mongoClientSettings);
		} catch (Exception e) {
		}
		return mongoClient;
	}

	@Override
	@Bean
	@Primary
	public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory, MappingMongoConverter converter) {
		return new MongoTemplate(mongoDbFactory, converter);
	}

	@Bean
	public MongoCustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<>();
		converters.add(DateToZonedDateTimeConverter.INSTANCE);
		converters.add(ZonedDateTimeToDateConverter.INSTANCE);
		return new MongoCustomConversions(converters);
	}

	enum DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

		INSTANCE;

		@Override
		public ZonedDateTime convert(Date source) {
			return source == null ? null : ofInstant(source.toInstant(), ZoneId.systemDefault());
		}
	}

	enum ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

		INSTANCE;

		@Override
		public Date convert(ZonedDateTime source) {
			return Date.from(source.toInstant());
		}
	}

	public static ZonedDateTime ofInstant(Instant instant, ZoneId systemDefault) {

		return instant.atZone(systemDefault);
	}

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return null;
	}
}
