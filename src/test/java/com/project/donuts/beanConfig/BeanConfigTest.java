package com.project.donuts.beanConfig;

import com.project.donuts.integration.config.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class BeanConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void test_getBean_by_functionName() {
        Bagel chocoBagel = (Bagel) context.getBean("chocolateBagel");
        assertEquals("chocolate", chocoBagel.flavour());
    }

    @Test
    public void test_getBean_by_classType_using_primary_annotation() {
        Bagel chocoBagel2 = context.getBean(Bagel.class);
        assertEquals("chocolate", chocoBagel2.flavour());
    }

    @Test
    public void test_getBean_by_qualifier_annotation() {
        BagelBox bagelBox = context.getBean(BagelBox.class);
        assertEquals("caramel", bagelBox.bagel().flavour());
        assertEquals(2, bagelBox.quantity());
    }

}
