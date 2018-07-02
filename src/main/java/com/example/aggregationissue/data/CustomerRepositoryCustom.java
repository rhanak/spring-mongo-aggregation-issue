package com.example.aggregationissue.data;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface CustomerRepositoryCustom {
    Map<List<Object>, List<Customer>> aggregate(Criteria criteriaIn, Sort sortBy, String... groupBy);
    Map<List<Object>, List<Customer>> findMatchingCustomers(Instant aggregationProcessingTime);
}
