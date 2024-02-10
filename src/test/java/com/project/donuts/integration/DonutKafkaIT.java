package com.project.donuts.integration;

import com.project.donuts.integration.config.IntegrationTest;
import com.project.donuts.kafka.MessagePublisher;
import com.project.donuts.web.Donut;
import com.project.donuts.web.DonutRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static com.project.donuts.kafka.KafkaConstants.RECEIVE_DONUTS_TOPIC;
import static com.project.donuts.kafka.KafkaConstants.SEND_DONUTS_TOPIC;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class DonutKafkaIT extends AbstractIntegration {

    @Autowired
    private DonutRepository donutRepository;

    @Autowired
    private MessagePublisher publisher;

    @Test
    public void publish_message_successfully() {
        String message = "Hello World";

        publisher.sendMessage(message, message, SEND_DONUTS_TOPIC);

        String sentMessage = sendDonutsTopicConsumer.getMessageByKey(message);
        assertNotNull(sentMessage);
        assertThat(sentMessage).isEqualTo(message);
    }

    @Test
    public void consume_message_successfully() {
        String message = "Hello World2";

        publisher.sendMessage(message, message, RECEIVE_DONUTS_TOPIC);
        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(
                () -> donutRepository.findByFlavour("flavour") != null
        );
    }

}