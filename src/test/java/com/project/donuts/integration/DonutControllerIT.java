package com.project.donuts.integration;

import com.project.donuts.integration.config.IntegrationTest;
import com.project.donuts.web.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class DonutControllerIT extends AbstractIntegration {

    @Autowired
    private DonutRepository donutRepository;

    private final String apiVersion = "v1";
    private final String donutsEndpoint = "/" + apiVersion + "/donuts";

    @BeforeEach
    public void init() {
        donutRepository.deleteAll();
    }

    @Test
    public void createDonut_should_create_new_donut_successfully() {
        DonutDTO request = new DonutDTO("chocolate", 16.5, 4);

        ResponseEntity<Donut> response = callCreateDonut(request);

        assertNull(response.getBody());
        assertNotNull(response.getHeaders().getLocation());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void sendDonuts_should_receive_request_and_return_correct_string_message() {
        String message = "Hello World";
        ResponseEntity<String> response = callSendDonuts(message);
        assertThat(response.getBody()).isEqualTo("Send donuts message published successfully");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void sendDonuts_should_publish_kafka_message_from_http_request() {
        String message1 = "Hello World One";
        callSendDonuts(message1);

        String publishedMessage1 = sendDonutsTopicConsumer.getMessageByKey(message1);
        assertNotNull(publishedMessage1);
        assertThat(publishedMessage1).isEqualTo(message1);
    }

    @Test
    public void getAllDonuts_should_fetch_all_donuts() {
        givenTwoTypesOfDonutsInInventory();
        ResponseEntity<Donuts> response = getAllDonuts();
        assertNotNull(response.getBody());
        assertThat(response.getBody().donuts.size()).isEqualTo(2);
    }

    @Test
    public void getDonut_should_return_donut() {
        // given
        Donut donut = donutRepository.save(new Donut("sugar-glazed", 16.3, 3));
        // when
        ResponseEntity<Donut> response = getDonutById(donut.getId());
        // then
        assertNotNull(response.getBody());
    }

    private ResponseEntity<Donut> callCreateDonut(DonutDTO newDonut) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "password");
        HttpEntity<DonutDTO> request = new HttpEntity<>(newDonut, headers);
        String url = "http://localhost:" + port + donutsEndpoint;

        return testRestTemplate.exchange(url, HttpMethod.POST, request, Donut.class);
    }

    private ResponseEntity<String> callSendDonuts(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "password");
        HttpEntity<String> request = new HttpEntity<>("String", headers);
        String sendDonutsEndpoint = "/" + apiVersion + "/donuts/send";
        String url = "http://localhost:" + port + sendDonutsEndpoint + "?message=" + message;

        return testRestTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    private ResponseEntity<Donuts> getAllDonuts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "password");
        HttpEntity<String> request = new HttpEntity<>("String", headers);
        String url = "http://localhost:" + port + donutsEndpoint;

        return testRestTemplate.exchange(url, HttpMethod.GET, request, Donuts.class);
    }

    private ResponseEntity<Donut> getDonutById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "password");
        HttpEntity<String> request = new HttpEntity<>("String", headers);
        String donutEndpoint = "/" + apiVersion + "/donuts/{id}";
        String url = "http://localhost:" + port + donutEndpoint.replace("{id}",id.toString());

        return testRestTemplate.exchange(url, HttpMethod.GET, request, Donut.class);
    }

    @Transactional
    private void givenTwoTypesOfDonutsInInventory() {
        donutRepository.save(new Donut("sugar-glazed", 16.3, 3));
        donutRepository.save(new Donut("chocolate", 16.0, 5));
    }

}