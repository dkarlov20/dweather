package com.dkarlov.dweather.core.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfiguration {

    @Value("${dweather.mongodb.url}")
    private String connectionString;

    @Value("${dweather.mongodb.database}")
    private String database;

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(database);
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(new ConnectionString(connectionString));
    }
}
