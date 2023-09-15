package com.project.donuts.integration.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Map;

/**
 * Since kafka does not allow us to search for arbitrary messages in a topic using a key,
 * what we do here is store all the messages published in a specific topic into a dedicated map of key-message pairs.
 * We then use this map to look up published messages using the message key.
 * In order for this approach to work, each topic must only have 1 consumer subscribed to it.
 */
public class CustomKafkaConsumer<K, V> {

    public final Consumer<K, V> consumer;
    public final Map<K, V> records;

    public CustomKafkaConsumer(Consumer<K, V> consumer, Map<K, V> records) {
        this.consumer = consumer;
        this.records = records;
    }

    // consider modifying to return ConsumerRecords instead
    public V getMessageByKey(K key) {
        ConsumerRecords<K, V> newRecords = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        newRecords.forEach(record -> records.put(record.key(), record.value()));
        return records.get(key);
    }
}