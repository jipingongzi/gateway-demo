package com.sean.gatewaydemo.reactor.name;

import java.util.Random;
import java.util.Arrays;
import java.util.List;
import reactor.core.publisher.Flux;

public class QueryService {

    private final List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

    public Flux<String> getAllNames() {
        Random random = new Random();
        return Flux.fromIterable(names)
                .delayElements(java.time.Duration.ofSeconds(1))
                .map(name -> {
                    // Simulate some delay and return the name
                    try {
                        Thread.sleep(1000); // Simulate I/O delay
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return names.get(random.nextInt(names.size()));
                });
    }
}