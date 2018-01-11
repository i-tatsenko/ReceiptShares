package com.receiptshares;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;


@SpringBootApplication(scanBasePackages = "com.receiptshares", exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class })
@EnableReactiveMongoRepositories
public class SplitItApplication {

    public static void main(String[] args) {
        SpringApplication.run(SplitItApplication.class, args);
    }

}
