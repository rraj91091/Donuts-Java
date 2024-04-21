package com.project.donuts.kafka;

import com.project.donuts.web.DonutDTO;
import com.project.donuts.web.DonutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.project.donuts.kafka.KafkaConstants.GROUP_ID;
import static com.project.donuts.kafka.KafkaConstants.RECEIVE_DONUTS_TOPIC;

/**
 * TODO - enhance consumer to get message key as well
 */
@Component
public class MessageConsumer {

    private final DonutService donutService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MessageConsumer(DonutService donutService) {
        this.donutService = donutService;
    }

    @KafkaListener(
            id = "donutsConsumerId",
            topics = RECEIVE_DONUTS_TOPIC,
            groupId = GROUP_ID
    )
    public void listen(
            @Payload String message
    ) {
        logger.info("Received message [" + message + "] from Topic [" + RECEIVE_DONUTS_TOPIC + "]");
        donutService.createDonut(new DonutDTO("flavour", 5.0, 2));
    }
}
