package org.anized.umf.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("production")
@PropertySource("classpath:/application.properties")
public class MongoConfig {
    private final Environment env;

    @Bean
    MongoTemplate mongoTemplate() {
        final MongoClient client = new MongoClient(
                getProperty("mongodb.host"),
                Integer.parseInt(getProperty("mongodb.port")));
        return new MongoTemplate(client, getProperty("mongodb.name"));
    }

    @Autowired
    public MongoConfig(final Environment env) {
        this.env = env;
    }

    private String getProperty(final String key) {
        return env.getRequiredProperty(key);
    }
}