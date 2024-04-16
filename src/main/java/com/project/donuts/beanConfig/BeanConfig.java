package com.project.donuts.beanConfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfig {

    @Bean
    public int quantity() {
        return 2;
    }

    @Bean
    @Primary
    public Bagel chocolateBagel() {
        return new Bagel("chocolate");
    }

    @Bean
    @Qualifier("caramel")
    public Bagel caramelBagel() {
        return new Bagel("caramel");
    }

    @Bean
    public BagelBox bagelBox(@Qualifier("caramel") Bagel bagel, int quantity) {
        return new BagelBox(bagel, quantity);
    }

    @Bean
    @Lazy
    @Qualifier("butter")
    public Bagel butterBagel() {
        return new Bagel("butter");
    }
}
