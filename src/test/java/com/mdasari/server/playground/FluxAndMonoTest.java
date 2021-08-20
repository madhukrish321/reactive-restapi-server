package com.mdasari.server.playground;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class FluxAndMonoTest {
  @Test
  public void fluxWithNames() {
    Flux<String> namesFlux = Flux.just("Madhu", "Lakshmi", "Karthikeya").log("fluxTest: ");

    namesFlux.subscribe(
        name -> log.info("Received element: " + name),
        exception ->
            log.error("Exception occurred while retrieving elements: ", exception.getMessage()),
        () -> log.info("No more items.."));
  }

  @Test
  public void fluxWithError() {
    Flux<String> namesFlux =
        Flux.just("Madhu", "Lakshmi", "Karthikeya")
            .concatWith(Flux.error(new RuntimeException("Throwing Runtime Exception for fun...")))
            .concatWith(Flux.just("Padmaja", "Srinivas"))
            .log("fluxErrorTest: ");

    namesFlux.subscribe(
        name -> log.info("Received element: " + name),
        exception ->
            log.error("Exception occurred while retrieving elements: ", exception.getMessage()),
        () -> log.info("No more items.."));
  }

  @Test
  public void fluxTestElements() {
    Flux<String> namesFlux = Flux.just("Madhu", "Lakshmi", "Karthikeya").log("fluxTestElements: ");

    /*
     * Reactor Test is the module that we are going to use to test the elements.
     * It has a class called StepVerifier which has some handy methods like expectNext(), expectCount(), etc...
     */
    StepVerifier.create(namesFlux)
        // .expectNext("Madhu", "Lakshmi", "Karthikeya") -> This is also valid
        .expectNext("Madhu")
        .expectNext("Lakshmi")
        .expectNext("Karthikeya")
        .verifyComplete(); // This is equivalent to subscribe method
  }

  @Test
  public void fluxTestElements_WithError1() {
    Flux<String> namesFlux =
        Flux.just("Madhu", "Lakshmi", "Karthikeya")
            .concatWith(Flux.error(new RuntimeException("Throwing RuntimeException for fun :)")))
            .log("fluxTestElements_WithError1: ");

    StepVerifier.create(namesFlux)
        .expectNext("Madhu", "Lakshmi", "Karthikeya")
        .verifyErrorMessage("Throwing RuntimeException for fun :)");
  }

  @Test
  public void fluxTestElements_WithError2() {
    Flux<String> namesFlux =
        Flux.just("Madhu", "Lakshmi", "Karthikeya")
            .concatWith(Flux.error(new RuntimeException("Throwing RuntimeException for fun :)")))
            .log("fluxTestElements_WithError2: ");

    StepVerifier.create(namesFlux)
        .expectNext("Madhu", "Lakshmi", "Karthikeya")
        .verifyError(RuntimeException.class);
  }

  @Test
  public void fluxTestElementsCount() {
    Flux<String> namesFlux =
        Flux.just("Madhu", "Lakshmi", "Karthikeya").log("fluxTestElementsCount: ");

    StepVerifier.create(namesFlux).expectNextCount(3).verifyComplete();
  }

  @Test
  public void monoTest() {
    Mono<String> nameMono = Mono.just("Madhu Krishna").log("monoTest: ");

    StepVerifier.create(nameMono).expectNext("Madhu Krishna").verifyComplete();
  }

  @Test
  public void monoTest_WithError() {
    StepVerifier.create(
            Mono.error(new RuntimeException("RuntimeException occurred..."))
                .log("monoTest_WithError: "))
        .verifyErrorMessage("RuntimeException occurred...");
  }
}
