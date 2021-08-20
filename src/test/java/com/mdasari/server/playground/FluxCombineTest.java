package com.mdasari.server.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxCombineTest {
  @Test
  public void combineUsingMerge() {
    Flux<String> flux1 = Flux.just("A", "B", "C");
    Flux<String> flux2 = Flux.just("D", "E", "F");
    Flux<String> alphabetsFlux = Flux.merge(flux1, flux2).log("combineUsingMerge: ");

    StepVerifier.create(alphabetsFlux).expectNextCount(6).verifyComplete();
  }

  @Test
  public void combineUsingMerge_withDelay() {
    Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
    Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

    /*
     * If we use merge() method, then we cannot predict the order
     * If order is important then we should use merge() method.
     * Note: If we merge() method then flux1 and flux2 emits elements in parallel
     * Note: If we concat() method then flux2 has to wait until flux1 emits all the elements
     */
    Flux<String> alphabetsFlux = Flux.merge(flux1, flux2).log("combineUsingMerge_withDelay: ");

    StepVerifier.create(alphabetsFlux).expectNextCount(6).verifyComplete();
  }

  @Test
  public void combineUsingConcat() {
    Flux<String> flux1 = Flux.just("A", "B", "C");
    Flux<String> flux2 = Flux.just("D", "E", "F");
    Flux<String> alphabetsFlux = Flux.concat(flux1, flux2).log("combineUsingConcat: ");

    StepVerifier.create(alphabetsFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  public void combineUsingConcat_withDelay() {
    Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
    Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

    Flux<String> alphabetsFlux = Flux.concat(flux1, flux2).log("combineUsingConcat_withDelay: ");

    StepVerifier.create(alphabetsFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  public void combineUsingZip() {
    Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
    Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

    Flux<String> stringFlux =
        Flux.zip(flux1, flux2, (e1, e2) -> e1.concat(e2)).log("combineUsingZip: ");

    StepVerifier.create(stringFlux).expectNext("AD", "BE", "CF").verifyComplete();
  }
}
