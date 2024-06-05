package com.sean.gatewaydemo.reactor.name;

import reactor.core.publisher.Flux;

public class Main {

    public static void main(String[] args) {
        QueryService queryService = new QueryService();
        NameCollectorService nameCollectorService = new NameCollectorService(queryService);

        // Collect names and print them when all elements are fetched
        Flux<String> nameFlux = nameCollectorService.collectNames();

        // Subscribe and block until completion
        nameFlux.blockLast();
    }
}
