package com.system.database;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.system.settings.AppSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

@Configuration
public class MongoConfig {

    @Bean
    public MongoDbFactory mongoDbFactory() {
        ConnectionString settings = new ConnectionString("mongodb://" + AppSettings.databaseDomain + ":" + AppSettings.databasePort);
        return new SimpleMongoClientDbFactory(MongoClients.create(settings), AppSettings.databaseName);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }

}