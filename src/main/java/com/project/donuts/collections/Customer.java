package com.project.donuts.collections;

import java.util.Objects;

public record Customer(String name, String role) {
    public static String CLASS_TYPE = "RECORD";

    public Customer {
        Objects.requireNonNull(name);
        Objects.requireNonNull(role);
    }
}
