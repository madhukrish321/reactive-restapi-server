package com.mdasari.server.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxFilterTest {
  List<String> names = Arrays.asList("Madhu", "Lakshmi", "Karthikeya");

  @Test
  public void filterTest() {
    Flux<String> namesFlux =
        Flux.fromIterable(names).filter(name -> name.startsWith("M")).log("filterTest: ");

    StepVerifier.create(namesFlux).expectNext("Madhu").verifyComplete();
  }

  @Test
  public void filterTestLength() {
    Flux<String> namesFlux =
        Flux.fromIterable(names).filter(name -> name.length() > 5).log("filterTestLength: ");

    StepVerifier.create(namesFlux).expectNext("Lakshmi", "Karthikeya").verifyComplete();
  }
}
