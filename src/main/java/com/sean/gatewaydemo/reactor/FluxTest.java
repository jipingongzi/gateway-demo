package com.sean.gatewaydemo.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class FluxTest {
    public static void main(String[] args) {
        List<String> words = Arrays.asList(
                "the", "quick", "brown", "fox",
                "jumped", "over", "the", "lazy", "dog");
        Flux.fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .concatWith(Mono.just("s")).distinct().sort()
                .zipWith(Flux.range(1, Integer.MAX_VALUE),
                        (string, count) ->
                                String.format("%2d. %s", count, string)
                )
                .subscribe(System.out::println);
    }
}
