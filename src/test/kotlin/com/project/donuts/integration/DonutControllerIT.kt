package com.project.donut.integration

import com.project.donut.database.Donut
import com.project.donuts.integration.AbstractIntegration
import com.project.donuts.integration.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.test.assertNotNull

@IntegrationTest
class DonutControllerIT(
    @Autowired val donutRepository: DonutRepository
) : AbstractIntegration() {

    private val apiVersion = "v1"
    private val createDonutEndpoint = "/$apiVersion/donuts/create"
    private val sendDonutsEndpoint = "/$apiVersion/donuts/send"
    private val getAllDonutsEndpoint = "/$apiVersion/donuts/all"

    @Test
    fun `createDonut should create a new donut successfully`() {
        val request = DonutDTO(
            flavour = "chocolate",
            diameter = 16.5,
            quantity = 4
        )

        val response = callCreateDonut(request)
        val newDonut = response.body!!
        assertThat(newDonut.flavour).isEqualTo("chocolate")
        assertThat(newDonut.diameter).isEqualTo(16.5)
        assertThat(newDonut.quantity).isEqualTo(4)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
    }

    @Test
    fun `sendDonuts should receive request and return correct string message`() {
        val message = "Hello World"
        val response = callSendDonuts(message)
        assertThat(response.body).isEqualTo("Send donuts message published successfully")
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `sendDonuts should publish kafka message from http request`() {
        val message1 = "Hello World"
        val message2 = "Hello World Two"
        callSendDonuts(message2)

        val publishedMessage2 = sendDonutsTopicConsumer.getMessageByKey(message2)
        assertNotNull(publishedMessage2)
        assertThat(publishedMessage2).isEqualTo(message2)

        val publishedMessage1 = sendDonutsTopicConsumer.getMessageByKey(message1)
        assertNotNull(publishedMessage1)
        assertThat(publishedMessage1).isEqualTo(message1)
    }

    @Test
    fun `getAllDonuts should fetch all donuts`() {
        givenTwoTypesOfDonutsInInventory()
        val response = getAllDonuts()
        assertThat(response.donuts.size).isEqualTo(2)
    }

    private fun callCreateDonut(newDonut: DonutDTO): ResponseEntity<Donut> {
        val request = HttpEntity(newDonut)
        return testRestTemplate!!.postForEntity(
            "http://localhost:$port$createDonutEndpoint",
            request,
            Donut::class.java
        )
    }

    private fun callSendDonuts(message: String): ResponseEntity<String> {
        val request = HttpEntity("String")
        return testRestTemplate!!.postForEntity(
            "http://localhost:$port$sendDonutsEndpoint?message=$message",
            request,
            String::class.java
        )
    }

    private fun getAllDonuts(): Donuts {
        return testRestTemplate!!.getForObject(
            "http://localhost:$port$getAllDonutsEndpoint",
            Donuts::class.java
        )
    }

    @Transactional
    private fun givenTwoTypesOfDonutsInInventory() {
        donutRepository.save(
            Donut(
                id = UUID.randomUUID(),
                flavour = "sugar-glazed",
                diameter = 16.3,
                quantity = 3
            )
        )

        donutRepository.save(
            Donut(
                id = UUID.randomUUID(),
                flavour = "sugar-glazed",
                diameter = 16.3,
                quantity = 3
            )
        )
    }
}