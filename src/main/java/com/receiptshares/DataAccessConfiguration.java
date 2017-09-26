package com.receiptshares;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientURI;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

import java.net.UnknownHostException;

@Configuration
public class DataAccessConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${data.db.mongo.url}")
    private String mongoUrl;

    @Value("${data.db.mongo.name}")
    private String dbName;

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUrl);
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Override
    public ReactiveMongoOperations reactiveMongoTemplate() throws Exception {
        return super.reactiveMongoTemplate();
    }

    @Override
    public ReactiveMongoDatabaseFactory mongoDbFactory() {
        try {
            return new SimpleReactiveMongoDatabaseFactory(new ConnectionString(mongoUrl + '/' + getDatabaseName()));
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(new SimpleMongoDbFactory(new MongoClientURI(mongoUrl + '/' + getDatabaseName())));
    }


}
