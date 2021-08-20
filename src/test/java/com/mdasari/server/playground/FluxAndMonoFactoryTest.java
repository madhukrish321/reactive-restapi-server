package com.mdasari.server.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {
  List<String> names = Arrays.asList("Madhu", "Lakshmi", "Karthikeya");

  @Test
  public void fluxUsingIterable() {
    Flux<String> namesFlux = Flux.fromIterable(names).log("fluxUsingIterable: ");
    StepVerifier.create(namesFlux).expectNextCount(3).verifyComplete();
  }

  @Test
  public void fluxUsingArray() {
    String[] namesArray = {"Madhu", "Lakshmi", "Karthikeya"};
    Flux<String> namesFlux = Flux.fromArray(namesArray).log("fluxUsingArray: ");
    StepVerifier.create(namesFlux).expectNext("Madhu", "Lakshmi", "Karthikeya").verifyComplete();
  }

  @Test
  public void fluxUsingStream() {
    Flux<String> namesFlux = Flux.fromStream(names.stream()).log("fluxUsingStream: ");
    StepVerifier.create(namesFlux).expectNextCount(3).verifyComplete();
  }

  @Test
  public void fluxUsingRange() {
    Flux<Integer> rangeFlux = Flux.range(1, 5).log("fluxUsingRange: ");
    StepVerifier.create(rangeFlux).expectNext(1, 2, 3, 4, 5).verifyComplete();
  }

  @Test
  public void monoUsingJustOrEmpty() {
    Mono<Object> emptyMono = Mono.justOrEmpty(null).log("monoUsingJustOrEmpty: ");
    StepVerifier.create(emptyMono).verifyComplete();
  }

  @Test
  public void monoUsingSupplier() {
    Supplier<String> nameSupplier = () -> "Madhu Krishna";
    Mono<String> nameMono = Mono.fromSupplier(nameSupplier).log("monoUsingSupplier: ");
    StepVerifier.create(nameMono).expectNext("Madhu Krishna").verifyComplete();
  }
}
