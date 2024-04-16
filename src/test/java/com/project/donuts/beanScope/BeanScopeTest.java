package com.project.donuts.beanScope;

import com.project.donuts.integration.config.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@IntegrationTest
public class BeanScopeTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void test_Prototype_And_Singleton_Bean_Values() {
        PrototypeClass prototype1 = context.getBean(PrototypeClass.class);
        PrototypeClass prototype2 = context.getBean(PrototypeClass.class);
        assertNotEquals(prototype1.getValue(), prototype2.getValue());

        SingletonClass singleton1 = context.getBean(SingletonClass.class);
        SingletonClass singleton2 = context.getBean(SingletonClass.class);
        assertEquals(singleton1.getValue(), singleton2.getValue());
    }

}