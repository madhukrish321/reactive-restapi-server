package com.mdasari.server.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {
  /*
   * Output:
   * ----------------
   * Subscriber1: A
   * Subscriber1: B
   * Subscriber2: A
   * Subscriber1: C
   * Subscriber2: B
   * Subscriber1: D
   * Subscriber2: C
   */
  @Test
  public void coldPublisherTest() throws InterruptedException {
    Flux<String> stringFlux =
        Flux.just("A", "B", "C", "D", "E", "F", "G").delayElements(Duration.ofSeconds(1));

    stringFlux.subscribe(
        e -> System.out.println("Subscriber1: " + e)); // Values will be emitted from beginning

    Thread.sleep(2000);

    stringFlux.subscribe(
        e -> System.out.println("Subscriber2: " + e)); // Values will be emitted from beginning

    Thread.sleep(3000);
  }

  /*
   * Output:
   * ----------------
   * Subscriber1: A
   * Subscriber1: B
   * Subscriber1: C
   * Subscriber2: C
   * Subscriber1: D
   * Subscriber2: D
   * Subscriber1: E
   * Subscriber2: E
   */
  @Test
  public void hotPublisherTest() throws InterruptedException {
    Flux<String> stringFlux =
        Flux.just("A", "B", "C", "D", "E", "F", "G").delayElements(Duration.ofSeconds(1));

    /*
     * ConnectableFlux will behave as Hot Publisher after we invoke the connect() method
     */
    ConnectableFlux<String> connectableFlux = stringFlux.publish();
    connectableFlux.connect();

    connectableFlux.subscribe(e -> System.out.println("Subscriber1: " + e));

    Thread.sleep(3000);

    connectableFlux.subscribe(
        e -> System.out.println("Subscriber2: " + e)); // Doesn't emit values from beginning

    Thread.sleep(3000);
  }
}
