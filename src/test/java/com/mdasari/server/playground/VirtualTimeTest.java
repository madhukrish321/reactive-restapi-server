package com.mdasari.server.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.function.Supplier;

public class VirtualTimeTest {
  @Test
  public void testingWithoutVirtualTime() {
    Flux<Long> intervalFlux =
        Flux.interval(Duration.ofMillis(1000)).take(4).log("testingWithoutVirtualTime: ");

    StepVerifier.create(intervalFlux)
        .expectSubscription()
        .expectNext(0L, 1L, 2L, 3L)
        .verifyComplete();
  }

  @Test
  public void testingWithVirtualTime() {
    VirtualTimeScheduler.getOrSet(); // This will enable virtual time

    Flux<Long> intervalFlux =
        Flux.interval(Duration.ofMillis(1000)).take(4).log("testingWithoutVirtualTime: ");

    Supplier<Flux<Long>> fluxSupplier = () -> intervalFlux;

    StepVerifier.withVirtualTime(fluxSupplier)
        .expectSubscription()
        .thenAwait(Duration.ofSeconds(4))
        .expectNext(0L, 1L, 2L, 3L)
        .verifyComplete();
  }
}
