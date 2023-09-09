package com.project.donuts.integration


import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka

@AutoConfigureEmbeddedDatabase(
    provider = DatabaseProvider.ZONKY,
    refresh = RefreshMode.AFTER_EACH_TEST_METHOD
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = [
        "listeners=PLAINTEXT://localhost:9092",
        "port=9092"
    ]
)
annotation class IntegrationTest
