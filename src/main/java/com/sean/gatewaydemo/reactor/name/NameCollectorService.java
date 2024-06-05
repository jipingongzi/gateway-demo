package com.sean.gatewaydemo.reactor.name;

import java.util.ArrayList;
import java.util.List;
import reactor.core.publisher.Flux;

public class NameCollectorService {

    private final QueryService queryService;

    public NameCollectorService(QueryService queryService) {
        this.queryService = queryService;
    }

    public Flux<String> collectNames() {
        List<String> collectedNames = new ArrayList<>();

        return queryService.getAllNames()
                .doOnNext(name -> {
                    collectedNames.add(name);
                    System.out.println("Collected Name: " + name);
                })
                .doOnComplete(() -> {
                    System.out.println("All names collected: " + collectedNames);
                })
                .doOnError(error -> {
                    System.err.println("Error occurred: " + error.getMessage());
                });
    }
}