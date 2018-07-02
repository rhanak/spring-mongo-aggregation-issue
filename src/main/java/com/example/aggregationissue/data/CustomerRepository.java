package com.example.aggregationissue.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String>, CustomerRepositoryCustom {

    Customer findByFirstName(String firstName);
}