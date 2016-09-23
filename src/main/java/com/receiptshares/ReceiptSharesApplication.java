package com.receiptshares;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = "com.receiptshares")
public class ReceiptSharesApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ReceiptSharesApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReceiptSharesApplication.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnProperty(name = "receipt.shares.mode", havingValue = "dev")
    public DataSource dataSource() {
        System.setProperty("spring.jpa.database-platform", "com.receiptshares.SQLiteDialect");
        return new DataSourceBuilder(ClassLoader.getSystemClassLoader())
                .driverClassName("org.sqlite.JDBC")
                .url("jdbc:sqlite:dev.db")
                .build();
    }

}
