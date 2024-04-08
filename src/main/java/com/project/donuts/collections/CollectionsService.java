package com.project.donuts.collections;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO unit tests
@Component
public class CollectionsService {

    public List<Customer> filterSortTransformCustomers(List<Customer> customers) {
        return customers.stream()
                .filter(e -> e.role().equalsIgnoreCase("Manager"))
                .sorted(Comparator.comparing(Customer::name)) // sort using custom comparator
                .map(e -> new Customer(e.name(), e.role().toUpperCase()))
                .peek(e -> System.out.println("Filtered value: " + e))
                .collect(Collectors.toList());
    }

    public void process(Stream<BigDecimal> input) {
        int groupSize = 3;
        int threshold = 30;
        AtomicInteger i = new AtomicInteger();
        System.out.println(
                input.filter(Objects::nonNull) // filter non-null
                        .filter(e -> e.compareTo(BigDecimal.ZERO) > 0) // filter negative values
                        .collect(Collectors.groupingBy(e -> i.getAndIncrement() / groupSize)) // groups into groups of 3 (creates a key-value map)
                        .values() // get values of map (returns a collection of List<BigDecimal>)
                        .stream()
                        .filter(e -> e.size() == groupSize) // filter value sets by size
                        .filter(e -> (e.stream()
                                        .reduce(BigDecimal.ZERO, BigDecimal::add) // sum up all values in list
                                        .divide(BigDecimal.valueOf(groupSize), RoundingMode.HALF_EVEN) // get average
                                        .compareTo(BigDecimal.valueOf(threshold)) < 0
                                )
                        )
                        .flatMap(Collection::stream) // converts a 2D collection into a 1D collection
                        .collect(Collectors.toList())
        );
    }

}

