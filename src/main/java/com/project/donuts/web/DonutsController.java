package com.project.donuts.web;

import com.project.donuts.kafka.KafkaConstants;
import com.project.donuts.kafka.MessagePublisher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Donut> createDonut(@RequestBody DonutDTO donut) {
        Donut newDonut = donutService.createDonut(donut);

        // add 'location' header so the consumer knows how to retrieve the created donut
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDonut.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // @CachePut(value="donuts", condition="#result.donuts.size()<50") // run function and cache results if condition is met
    @Cacheable("donuts")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public Donuts getAllDonuts() {
        return new Donuts(donutService.getDonuts());
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public Donut getDonut(@PathVariable(name = "id") String id) {
        return donutService.getDonutById(UUID.fromString(id));
    }

    @PostMapping(value = "/send", produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public String sendDonuts(@RequestParam("message") String message) {
        messagePublisher.sendMessage(message, message, KafkaConstants.SEND_DONUTS_TOPIC);
        return "Send donuts message published successfully";
    }

}
