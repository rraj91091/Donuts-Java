package com.project.donuts.integration

import org.apache.kafka.clients.consumer.Consumer
import org.springframework.kafka.test.utils.KafkaTestUtils

/**
 * Since kafka does not allow us to search for arbitrary messages in a topic using a key,
 * what we do here is store all the messages published in a specific topic into a dedicated map of key-message pairs.
 * We then use this map to look up published messages using the message key.
 * In order for this approach to work, each topic must only have 1 consumer subscribed to it.
 */
data class CustomKafkaConsumer<K, V>(
    val consumer: Consumer<K, V>,
    val records: MutableMap<K, V>
) {

    // consider modifying to return ConsumerRecords instead
    fun getMessageByKey(key: K): V? {
        val newRecords = KafkaTestUtils.getRecords(consumer, 5000)
        newRecords.forEach { record ->
            records[record.key()] = record.value()
        }
        return records[key]
    }
}