package com.project.donuts.web;

import com.project.donuts.kafka.KafkaConstants;
import com.project.donuts.kafka.MessagePublisher;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;

// @CacheConfig(cacheNames={"donuts"}) //possible alternative to CacheConfig class
@RestController
@RequestMapping(value = "/v1/donuts")
public class DonutsController {

    private final DonutService donutService;
    private final MessagePublisher messagePublisher;
    private final MessageSource messageSource;

    public DonutsController(DonutService donutService, MessagePublisher messagePublisher, MessageSource messageSource) {
        this.donutService = donutService;
        this.messagePublisher = messagePublisher;
        this.messageSource = messageSource;
    }

    // `produces` MediaTypes are optional unless we want to restrict the types to be returned
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Donut> createDonut(@Valid @RequestBody DonutDTO donut) {
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
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Donuts getAllDonuts() {
        return new Donuts(donutService.getDonuts());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Donut getDonut(@PathVariable(name = "id") String id) {
        Donut donut = donutService.getDonutById(UUID.fromString(id));
        if (donut == null) {
            throw new DonutNotFoundException("Donut Not Found for ID: " + id);
        }
        return donut;
    }

    @PostMapping(value = "/send")
    @ResponseStatus(value = HttpStatus.OK)
    public String sendDonuts(@RequestParam("message") String message) {
        messagePublisher.sendMessage(message, message, KafkaConstants.SEND_DONUTS_TOPIC);
        return "Send donuts message published successfully";
    }

    @GetMapping(value = "/internationalization")
    @ResponseStatus(value = HttpStatus.OK)
    public String internationalization() {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage("message.good-morning",null,"Default Message", locale);
    }

}
