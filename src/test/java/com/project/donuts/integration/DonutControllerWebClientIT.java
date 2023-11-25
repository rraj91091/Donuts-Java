package com.project.donuts.integration;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import com.project.donuts.kafka.MessagePublisher;
import com.project.donuts.web.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * For testing only the controller class
 */
@WebFluxTest(controllers = {DonutsController.class})
public class DonutControllerWebClientIT {

    @Autowired
    ApplicationContext context;

    WebTestClient webTestClient;

    @MockBean
    private DonutService donutService;

    @MockBean
    private MessagePublisher messagePublisher;

    private final String apiVersion = "v1";

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void createDonut_should_create_new_donut_successfully() {
        when(donutService.createDonut(any())).thenReturn(new Donut("chocolate", 16.5, 4));

        webTestClient.post()
                .uri(URI.create("/" + apiVersion + "/donuts/create"))
                .bodyValue(new DonutDTO("chocolate", 16.5, 4))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("flavour").isEqualTo("chocolate")
                .jsonPath("diameter").isEqualTo(16.5)
                .jsonPath("quantity").isEqualTo(4);
    }

    @Test
    public void sendDonuts_should_receive_request_and_return_correct_string_message() throws URISyntaxException {
        String message = "Send Donuts";

        URI uri = new URIBuilder("/" + apiVersion + "/donuts/send")
                .addParameter("message", message)
                .build();

        webTestClient.post()
                .uri(uri)
                .bodyValue(message)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Send donuts message published successfully");
    }

    @Test
    public void
    getAllDonuts_should_fetch_all_donuts() {
        Donut donut1 = new Donut("sugar-glazed", 16.3, 3);
        Donut donut2 = new Donut("chocolate", 16.0, 5);
        when(donutService.getDonuts()).thenReturn(Arrays.asList(donut1, donut2));

        Donuts response = webTestClient.get()
                .uri(URI.create("/" + apiVersion + "/donuts/all"))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Donuts.class)
                .returnResult().getResponseBody();

        assert response != null;
        assert response.donuts.size() == 2;
    }

}