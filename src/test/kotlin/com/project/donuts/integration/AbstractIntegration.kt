package com.project.donuts.integration

import com.project.donut.kafka.config.KafkaConstants.Companion.GROUP_ID
import com.project.donut.kafka.config.KafkaConstants.Companion.SEND_DONUTS_TOPIC
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.common.serialization.StringDeserializer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.utils.KafkaTestUtils
import java.util.*
import javax.sql.DataSource
import kotlin.collections.HashMap
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractIntegration {

    @Value(value = "\${local.server.port}")
    val port = 0

    @Autowired
    val testRestTemplate: TestRestTemplate? = null

    @Autowired
    val dataSource: DataSource? = null

    @Autowired
    val embeddedKafkaBroker: EmbeddedKafkaBroker? = null

    lateinit var sendDonutsTopicConsumer: CustomKafkaConsumer<String, String>

    @BeforeAll
    fun setup() {
        assertNotNull(testRestTemplate, "Warning: testRestTemplate is null!")
        assertNotNull(embeddedKafkaBroker, "Warning: embeddedKafkaBroker is null!")

        sendDonutsTopicConsumer = configureConsumer(SEND_DONUTS_TOPIC, GROUP_ID)
    }

    @AfterAll
    fun teardown() {
        sendDonutsTopicConsumer.consumer.close()
    }

    private fun configureConsumer(topic: String, groupId: String): CustomKafkaConsumer<String, String> {
        val consumerProps: MutableMap<String, Any> =
            KafkaTestUtils.consumerProps(groupId, "true", embeddedKafkaBroker)
        consumerProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        consumerProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        val consumer: Consumer<String, String> = DefaultKafkaConsumerFactory<String, String>(consumerProps)
            .createConsumer()
        consumer.subscribe(Collections.singleton(topic))
        return CustomKafkaConsumer(consumer, hashMapOf())
    }

    private fun configureProducer(): Producer<String, String> {
        val producerProps: Map<String, Any> = HashMap(KafkaTestUtils.producerProps(embeddedKafkaBroker))
        return DefaultKafkaProducerFactory<String, String>(producerProps).createProducer()
    }

}


