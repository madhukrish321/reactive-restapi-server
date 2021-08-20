package com.mdasari.server.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxBackPressureTest {
  @Test
  public void backPressureTest() {
    Flux<Integer> rangeFlux = Flux.range(1, 10).log("backPressureTest: ");
    StepVerifier.create(rangeFlux)
        .expectSubscription()
        .thenRequest(2) // We are requesting first 2 elements
        .expectNext(1, 2)
        .thenRequest(3) // We are requesting next 3 elements
        .expectNext(3, 4, 5)
        .thenCancel()
        .verify();
  }

  @Test
  public void backPressure_withRequest() {
    /*
     * Using request(long number) method of Subscription object we are requesting the Publisher to publish only n elements
     */
    Flux<Integer> rangeFlux = Flux.range(1, 10).log("backPressure_withRequest: ");
    rangeFlux.subscribe(
        value -> System.out.println("Value is: " + value),
        exp -> System.out.println("Exception occurred: " + exp.getMessage()),
        () -> System.out.println("No more elements..."),
        subscription -> subscription.request(3));
  }

  @Test
  public void backPressure_withCancel() {
    Flux<Integer> rangeFlux = Flux.range(1, 10).log("backPressure_withCancel: ");
    rangeFlux.subscribe(
        value -> System.out.println("Value is: " + value),
        exp -> System.out.println("Exception occurred: " + exp.getMessage()),
        () -> System.out.println("No more elements..."),
        subscription -> subscription.cancel());
  }

  @Test
  public void backPressure_withBaseSubscriber() {
    Flux<Integer> rangeFlux = Flux.range(1, 10).log("backPressure_withCancel: ");
    rangeFlux.subscribe(
        new BaseSubscriber<Integer>() {
          @Override
          protected void hookOnNext(Integer value) {
            request(1); // We are requesting 1 element at a time
            System.out.println("Value: " + value);
            if (value == 4) {
              cancel(); // Stream will be cancelled with the value reaches 4
            }
          }
        });
  }
}
