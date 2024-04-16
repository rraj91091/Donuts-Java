package com.project.donuts.beanScope;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SingletonClass {

    private final UUID value = UUID.randomUUID();

    public UUID getValue() {
        return value;
    }
}
