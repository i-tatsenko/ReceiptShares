package com.receiptshares;

import com.mongodb.MongoClientURI;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class DataAccessConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${data.db.mongo.url}")
    private String mongoUrl;

    @Value("${data.db.mongo.name}")
    private String dbName;

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(mongoUrl + '/' + getDatabaseName());
    }

    @Override
    public ReactiveMongoOperations reactiveMongoTemplate() throws Exception {
        return super.reactiveMongoTemplate();
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(new SimpleMongoDbFactory(new MongoClientURI(mongoUrl + '/' + getDatabaseName())));
    }


}
