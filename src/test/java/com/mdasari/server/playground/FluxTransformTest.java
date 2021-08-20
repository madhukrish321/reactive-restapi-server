package com.mdasari.server.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxTransformTest {
  List<String> names = Arrays.asList("Madhu", "Lakshmi", "Karthikeya");

  @Test
  public void transformUsingMap() {
    Flux<String> namesFlux =
        Flux.fromIterable(names).map(String::toUpperCase).log("transformUsingMap: ");

    StepVerifier.create(namesFlux).expectNext("MADHU", "LAKSHMI", "KARTHIKEYA").verifyComplete();
  }

  @Test
  public void transformUsingMap_Length() {
    Flux<Integer> namesFlux =
        Flux.fromIterable(names).map(String::length).log("transformUsingMap_Length: ");

    StepVerifier.create(namesFlux).expectNext(5, 7, 10).verifyComplete();
  }

  @Test
  public void transformUsingMap_Repeat() {
    Flux<Integer> namesFlux =
        Flux.fromIterable(names).map(String::length).repeat(1).log("transformUsingMap_Repeat: ");

    StepVerifier.create(namesFlux).expectNext(5, 7, 10, 5, 7, 10).verifyComplete();
  }

  @Test
  public void transformUsingFilter_Map() {
    Flux<String> namesFlux =
        Flux.fromIterable(names)
            .filter(name -> name.length() > 5)
            .map(String::toUpperCase)
            .log("transformUsingFilter_Map: ");

    StepVerifier.create(namesFlux).expectNext("LAKSHMI", "KARTHIKEYA").verifyComplete();
  }

  @Test
  public void transformUsingFlatMap() {
    /*
     * We can use flatMap when we are making an external or db call that returns a Flux
     */
    Flux<String> elementsFlux =
        Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E"))
            .flatMap(
                element -> {
                  // The below line will return Flux<String>. That's why we are using flatMap
                  return Flux.fromIterable(getList(element));
                })
            .log("transformUsingFlatMap");

    StepVerifier.create(elementsFlux)
        .expectNext(
            "A",
            "New Value",
            "B",
            "New Value",
            "C",
            "New Value",
            "D",
            "New Value",
            "E",
            "New Value")
        .verifyComplete();
  }

  @Test
  public void transformUsingFlatMap_Parallel() {
    Flux<String> elementsFlux =
        Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
            .window(2) // Flux<Flux<String> (A, B), (C, D), (E, F)
            .flatMap(
                flux ->
                    flux.map(element -> getList(element))
                        .subscribeOn(Schedulers.parallel())) // Flux<List<String>
            .flatMap(list -> Flux.fromIterable(list)) // Flux<String>
            .log("transformUsingFlatMap_Parallel");

    StepVerifier.create(elementsFlux).expectNextCount(12).verifyComplete();
  }

  @Test
  public void transformUsingFlatMap_Parallel_MaintainOrder() {
    Flux<String> elementsFlux =
        Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
            .window(2) // Flux<Flux<String> (A, B), (C, D), (E, F)
            .flatMapSequential(
                flux ->
                    flux.map(element -> getList(element))
                        .subscribeOn(Schedulers.parallel())) // Flux<List<String>
            .flatMap(list -> Flux.fromIterable(list)) // Flux<String>
            .log("transformUsingFlatMap_Parallel_MaintainOrder");

    StepVerifier.create(elementsFlux).expectNextCount(12).verifyComplete();
  }

  private List<String> getList(String element) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return Arrays.asList(element, "New Value");
  }
}
