package com.example.aggregationissue.data;

import com.mongodb.DBObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

@Repository
public class CustomerRepositoryDelegate implements CustomerRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    public CustomerRepositoryDelegate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Map<List<Object>, List<Customer>> aggregate(Criteria criteriaIn, Sort sortBy, String... groupBy) {

        Map<List<Object>, List<Customer>> aggregatedAlerts = new HashMap<>();

        // create and run the aggregation pipeline
        Aggregation agg = Aggregation.newAggregation(Customer.class,
                Aggregation.match(criteriaIn),
                limit(1000),
                Aggregation.sort(sortBy),
                Aggregation.group(groupBy));

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg,
                mongoTemplate.getCollectionName(Customer.class), DBObject.class);

        // Format the group keys for this result
        Object[] keys = new Object[groupBy.length];

        // Get the list of customers for each group, and map it to the group key
        List<Customer> customersList = new ArrayList<>();

        for (DBObject customerObject : results.getMappedResults()) {

            // bind the attributes back to an customer object
            Customer customer = mongoTemplate.getConverter().read(Customer.class, customerObject);
            Map<String, Object> embeddedCustomer = customer.getCustomer();
            if (embeddedCustomer == null) {
                embeddedCustomer = new HashMap<>();
                customer.setCustomer(embeddedCustomer);
            }
            customersList.add(customer);
        }

        // map the customers back to the group key
        aggregatedAlerts.put(Arrays.asList(keys), customersList);

        return aggregatedAlerts;
    }

    public Map<List<Object>, List<Customer>> findMatchingCustomers(Instant aggregationProcessingTime) {
        Criteria dbCriteria = Criteria.where("receivedTime").lte(Date.from(aggregationProcessingTime));
        Sort sortBy = new Sort(Sort.Direction.ASC, "customer.timestamp");
        Map<List<Object>, List<Customer>> result = aggregate(dbCriteria, sortBy, "customer.timestamp");
        return result;
    }
}