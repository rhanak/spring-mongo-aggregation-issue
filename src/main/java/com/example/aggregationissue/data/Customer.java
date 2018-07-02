package com.example.aggregationissue.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "customer")
public class Customer {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Date receivedTime;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JsonProperty(value = "customer")
    @Field(value = "customer")
    private Map<String, Object> normalizedCustomer;

    public Customer(String firstName, String lastName, Instant receivedTime, Map<String, Object> normalizedCustomer) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.receivedTime = Date.from(receivedTime);
        setCustomer(normalizedCustomer);
    }

    public Map<String, Object> getCustomer() {
        return normalizedCustomer;
    }

    public void setCustomer(Map<String, Object> customer) {
        this.normalizedCustomer = customer;
    }
}