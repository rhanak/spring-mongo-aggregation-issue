package com.example.aggregationissue.config;

import com.example.aggregationissue.data.Repositories;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(repositoryImplementationPostfix = "Delegate", basePackageClasses = Repositories.class)
public class MongoConfiguration {
}
