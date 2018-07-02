package com.example.aggregationissue;

import com.example.aggregationissue.data.Customer;
import com.example.aggregationissue.data.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class AggregationIssueApplication  implements CommandLineRunner {

	@Autowired
	private CustomerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(AggregationIssueApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		Map<String, Object> propsBob = new HashMap<>();
		propsBob.put("address", "PO Box 123. Washington, DC");
		propsBob.put("timestamp", Date.from(Instant.parse("2017-01-06T11:51:31.000Z")));

		Map<String, Object> propsJohn = new HashMap<>();
		propsJohn.put("address", "PO Box 456. Washington, DC");
		propsJohn.put("timestamp", Date.from(Instant.parse("2017-02-03T11:51:12.000Z")));

		// save a couple of customers
		repository.save(new Customer("Bob", "Smith", Instant.parse("2017-01-06T11:51:31.000Z"), propsBob));
		repository.save(new Customer("John", "Smith", Instant.parse("2017-02-03T11:51:31.000Z"), propsJohn));

		System.out.println("Customers found with agg(now)");
		System.out.println("--------------------------------");
		Map<List<Object>, List<Customer>> pairs = repository.findMatchingCustomers(Instant.now());
		for (Map.Entry<List<Object>, List<Customer>> entry : pairs.entrySet()) {
			for(Customer customer : entry.getValue()) {
				System.out.println(customer);
			}
		}
	}

}
