package com.receiptshares;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;

@Configuration
public class DataAccessConfiguration extends AbstractReactiveMongoConfiguration {

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    @Override
    protected String getDatabaseName() {
        return "receiptshares";
    }

    @Override
    public ReactiveMongoOperations reactiveMongoTemplate() throws Exception {
        return super.reactiveMongoTemplate();
    }
}
