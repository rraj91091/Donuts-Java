package com.project.donuts.integration;

import com.project.donuts.integration.config.IntegrationTest;
import com.project.donuts.web.Donut;
import com.project.donuts.web.DonutDTO;
import com.project.donuts.web.DonutRepository;
import com.project.donuts.web.Donuts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class DonutControllerIT extends AbstractIntegration {

    @Autowired
    private DonutRepository donutRepository;

    private final String apiVersion = "v1";

    @Nested
    @DisplayName("Tests using TestRestTemplate")
    class UsingTestRestTemplate {
        @Test
        public void createDonut_should_create_new_donut_successfully() {
            DonutDTO request = new DonutDTO("chocolate", 16.5, 4);

            ResponseEntity<Donut> response = callCreateDonut(request);
            Donut newDonut = response.getBody();
            assertNotNull(newDonut);
            assertThat(newDonut.getFlavour()).isEqualTo("chocolate");
            assertThat(newDonut.getDiameter()).isEqualTo(16.5);
            assertThat(newDonut.getQuantity()).isEqualTo(4);
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
        public void
        getAllDonuts_should_fetch_all_donuts() {
            givenTwoTypesOfDonutsInInventory();
            Donuts response = getAllDonuts();
            assertThat(response.donuts.size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Tests using TestWebClient")
    class UsingTestWebClient {
        //TODO create another set of tests using WebTestClient
    }

    private ResponseEntity<Donut> callCreateDonut(DonutDTO newDonut) {
        HttpEntity<DonutDTO> request = new HttpEntity<>(newDonut);
        String createDonutEndpoint = "/" + apiVersion + "/donuts/create";
        String url = "http://localhost:" + port + createDonutEndpoint;
        return testRestTemplate.postForEntity(url, request, Donut.class);
    }

    private ResponseEntity<String> callSendDonuts(String message) {
        HttpEntity<String> request = new HttpEntity<>("String");
        String sendDonutsEndpoint = "/" + apiVersion + "/donuts/send";
        String url = "http://localhost:" + port + sendDonutsEndpoint + "?message=" + message;
        return testRestTemplate.postForEntity(url, request, String.class);
    }

    private Donuts getAllDonuts() {
        String getAllDonutsEndpoint = "/" + apiVersion + "/donuts/all";
        String url = "http://localhost:" + port + getAllDonutsEndpoint;
        return testRestTemplate.getForObject(url, Donuts.class);
    }

    @Transactional
    private void givenTwoTypesOfDonutsInInventory() {
        donutRepository.save(new Donut("sugar-glazed", 16.3, 3));
        donutRepository.save(new Donut("chocolate", 16.0, 5));
    }
}