package com.sean.gatewaydemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/merge")
public class MergeController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/get")
    public Mono<String> getMerge(){
        WebClient webClient = webClientBuilder.build();
        Mono<String> result1 = webClient.get().uri("http://localhost:8080/user/api/info").retrieve().bodyToMono(String.class);
        Mono<String> result2 = webClient.get().uri("http://localhost:8080/order/api/info").retrieve().bodyToMono(String.class);
        return result1.zipWith(result2, (a, b) -> a + b);

    }
}
