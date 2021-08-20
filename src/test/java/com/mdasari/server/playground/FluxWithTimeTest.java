package com.mdasari.server.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxWithTimeTest {
  @Test
  public void infiniteSequence() throws InterruptedException {
    Flux<Long> sequenceFlux =
        Flux.interval(Duration.ofMillis(200)).log("infiniteSequence: "); // 0,1,2,3,4....
    sequenceFlux.subscribe(System.out::println);
    Thread.sleep(3000); // We need to add this to check how the elements are emitted.
  }

  @Test
  public void finiteSequenceTest() {
    /*
     * take(n) -> It will only add n number of elements to the Flux
     */
    Flux<Long> sequenceFlux =
        Flux.interval(Duration.ofMillis(200)).take(3).log("finiteSequenceTest: "); // 0,1,2

    StepVerifier.create(sequenceFlux).expectSubscription().expectNext(0L, 1L, 2L).verifyComplete();
  }

  @Test
  public void finiteSequenceTestMap() {
    /*
     * take(n) -> It will only add n number of elements to the Flux
     */
    Flux<Long> sequenceFlux =
        Flux.interval(Duration.ofMillis(200))
            .take(3)
            .map(num -> num + 21)
            .log("finiteSequenceTestMap: "); // 21,22,23

    StepVerifier.create(sequenceFlux)
        .expectSubscription()
        .expectNext(21L, 22L, 23L)
        .verifyComplete();
  }

  @Test
  public void finiteSequenceTestMap_withDelay() {
    /*
     * take(n) -> It will only add n number of elements to the Flux
     */
    Flux<Long> sequenceFlux =
        Flux.interval(Duration.ofMillis(200))
            .delayElements(Duration.ofSeconds(1))
            .take(3)
            .map(num -> num + 21)
            .log("finiteSequenceTestMap_withDelay: "); // 21,22,23

    StepVerifier.create(sequenceFlux)
        .expectSubscription()
        .expectNext(21L, 22L, 23L)
        .verifyComplete();
  }
}
