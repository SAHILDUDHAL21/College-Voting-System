package com.sahil.votethegoat.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        // Updated to include tlsInsecure for local testing environments with network restrictions
        String uri = "mongodb+srv://p9897401_db_user:AiW5ILIP0QN3LjB4@sahild11.ekgaukq.mongodb.net/votingsystem?retryWrites=true&w=majority&appName=Sahild11&tls=true&tlsInsecure=true";
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "votingsystem");
    }
}
