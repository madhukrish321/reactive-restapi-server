package com.mdasari.server.playground;

import com.mdasari.server.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class FluxErrorTest {
  @Test
  public void fluxErrorHandling_onErrorResume() {
    Flux<String> stringFlux =
        Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("Throwing RuntimeException for fun")))
            .concatWith(Flux.just("D"))
            .onErrorResume(
                exp -> {
                  log.error("Exception Details: " + exp.getMessage());
                  return Flux.just("E", "F");
                })
            .log("fluxErrorHandling_onErrorResume: ");

    StepVerifier.create(stringFlux).expectNext("A", "B", "C", "E", "F").verifyComplete();
  }

  @Test
  public void fluxErrorHandling_onErrorReturn() {
    Flux<String> stringFlux =
        Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("Throwing RuntimeException for fun")))
            .concatWith(Flux.just("D"))
            .onErrorReturn("E")
            .log("fluxErrorHandling_onErrorReturn: ");

    StepVerifier.create(stringFlux).expectNext("A", "B", "C", "E").verifyComplete();
  }

  @Test
  public void fluxErrorHandling_onErrorMap() {
    Flux<String> stringFlux =
        Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("Throwing RuntimeException for fun")))
            .concatWith(Flux.just("D"))
            .onErrorMap(exp -> new CustomException(exp.getMessage()))
            .log("fluxErrorHandling_onErrorReturn: ");

    StepVerifier.create(stringFlux).expectNext("A", "B", "C").verifyError(CustomException.class);
  }

  @Test
  public void fluxErrorHandling_withRetry() {
    Flux<String> stringFlux =
        Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("Throwing RuntimeException for fun")))
            .concatWith(Flux.just("D"))
            .retry(2)
            .log("fluxErrorHandling_onErrorReturn: ");

    StepVerifier.create(stringFlux)
        .expectNext("A", "B", "C", "A", "B", "C", "A", "B", "C")
        .verifyError(RuntimeException.class);
  }
}
