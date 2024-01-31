package com.project.donuts.collections;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CollectionsService {

    public List<Customer> filterSortTransformCustomers(List<Customer> customers) {
        return customers.stream()
                .filter(e -> e.role().equalsIgnoreCase("Manager"))
                .sorted(Comparator.comparing(Customer::name))
                .map(e -> new Customer(e.name(), e.role().toUpperCase()))
                .peek(e -> System.out.println("Filtered value: " + e))
                .collect(Collectors.toList());
    }

}

