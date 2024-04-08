package com.project.donuts.web;

import com.project.donuts.kafka.KafkaConstants;
import com.project.donuts.kafka.MessagePublisher;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

// @CacheConfig(cacheNames={"donuts"}) //possible alternative to CacheConfig class
@RestController
@RequestMapping(value = "/v1/donuts")
public class DonutsController {

    private final DonutService donutService;
    private final MessagePublisher messagePublisher;

    public DonutsController(DonutService donutService, MessagePublisher messagePublisher) {
        this.donutService = donutService;
        this.messagePublisher = messagePublisher;
    }

    @PostMapping(value = "/create/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    public Donut createDonut(@RequestBody DonutDTO donut, @PathVariable(name = "id") String id) {
        return donutService.createDonut(donut);
    }

    @PostMapping(value = "/send", produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public String sendDonuts(@RequestParam("message") String message) {
        messagePublisher.sendMessage(message, message, KafkaConstants.SEND_DONUTS_TOPIC);
        return "Send donuts message published successfully";
    }

    // @CachePut(value="donuts", condition="#result.donuts.size()<50") // run function and cache results if condition is met
    @Cacheable("donuts")
    @GetMapping(value = "/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public Donuts getAllDonuts() {
        return new Donuts(donutService.getDonuts());
    }

}
