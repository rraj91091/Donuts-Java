package com.project.donuts.web;

import com.project.donuts.kafka.KafkaConstants;
import com.project.donuts.kafka.MessagePublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/donuts")
public class DonutsController {

    private final DonutService donutService;
    private final MessagePublisher messagePublisher;

    public DonutsController(DonutService donutService, MessagePublisher messagePublisher) {
        this.donutService = donutService;
        this.messagePublisher = messagePublisher;
    }

    @PostMapping(value = "/create", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    public Donut createDonut(@RequestBody DonutDTO donut) {
        System.out.println("HEllo WORLD!!!");
        return donutService.createDonut(donut);
    }

    @PostMapping(value = "/send", produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public String sendDonuts(@RequestParam("message") String message) {
        messagePublisher.sendMessage(message, message, KafkaConstants.SEND_DONUTS_TOPIC);
        return "Send donuts message published successfully";
    }

    @GetMapping(value = "/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public Donuts getAllDonuts() {
        return new Donuts(donutService.getDonuts());
    }

}