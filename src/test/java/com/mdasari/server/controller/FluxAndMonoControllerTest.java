package com.mdasari.server.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

/*
 * @WebFluxTest is responsible to register classes that are annotated with @Controller and @RestController
 * Note: It doesn't register classes that are annotated with @Component, @Service, @Repository, etc...
 */
@WebFluxTest
@AutoConfigureWebTestClient(timeout = "36000")
public class FluxAndMonoControllerTest {

  @Autowired private WebTestClient webTestClient;

  @Test
  void returnFlux_approach1() {
    Flux<Integer> integerFlux =
        webTestClient
            .get()
            .uri("/flux")
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(Integer.class)
            .getResponseBody();

    StepVerifier.create(integerFlux).expectSubscription().expectNext(1, 2, 3, 4).verifyComplete();
  }

  @Test
  void returnFlux_approach2() {
    webTestClient
        .get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Integer.class)
        .hasSize(4);
  }

  @Test
  void returnFlux_approach3() {
    List<Integer> expectedResult = Arrays.asList(1, 2, 3, 4);
    EntityExchangeResult<List<Integer>> entityExchangeResult =
        webTestClient
            .get()
            .uri("/flux")
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Integer.class)
            .returnResult();
    Assertions.assertEquals(expectedResult, entityExchangeResult.getResponseBody());
  }

  @Test
  void returnFlux_approach4() {
    List<Integer> expectedResult = Arrays.asList(1, 2, 3, 4);
    webTestClient
        .get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Integer.class)
        .consumeWith(
            response -> Assertions.assertEquals(expectedResult, response.getResponseBody()));
  }

  @Test
  void returnFluxStreamTest() {
    Flux<Long> longStreamFlux =
        webTestClient
            .get()
            .uri("/flux/stream")
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
            .returnResult(Long.class)
            .getResponseBody();

    StepVerifier.create(longStreamFlux)
        .expectSubscription()
        .expectNext(1L)
        .expectNext(2L)
        .expectNext(3L)
        .thenCancel()
        .verify();
  }

  @Test
  void returnMonoTest() {
    webTestClient
        .get()
        .uri("/mono")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Integer.class)
        .consumeWith(
            response -> Assertions.assertEquals(Integer.valueOf(21), response.getResponseBody()));
  }
}
