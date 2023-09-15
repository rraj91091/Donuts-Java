package com.project.donuts.integration;

import com.project.donuts.integration.config.CustomKafkaConsumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.project.donuts.kafka.KafkaConstants.GROUP_ID;
import static com.project.donuts.kafka.KafkaConstants.SEND_DONUTS_TOPIC;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIntegration {

    @Value(value = "${local.server.port}")
    int port = 0;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    DataSource dataSource;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    CustomKafkaConsumer<String, String> sendDonutsTopicConsumer;

    @BeforeAll
    public void setup() {
        assertNotNull(testRestTemplate, "Warning: testRestTemplate is null!");
        assertNotNull(embeddedKafkaBroker, "Warning: embeddedKafkaBroker is null!");

        sendDonutsTopicConsumer = configureConsumer(SEND_DONUTS_TOPIC, GROUP_ID);
    }

    @AfterAll
    public void teardown() {
        sendDonutsTopicConsumer.consumer.close();
    }

    private CustomKafkaConsumer<String, String> configureConsumer(String topic, String groupId) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(groupId, "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(Collections.singleton(topic));
        return new CustomKafkaConsumer<>(consumer, new HashMap<>());
    }

    private Producer<String, String> configureProducer() {
        Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        return new DefaultKafkaProducerFactory<String, String>(producerProps).createProducer();
    }

}
