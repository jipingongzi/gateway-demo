package com.sean.gatewaydemo.webdemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/open-api")
public class Rest {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/test")
    public Mono<RestVo> test(@RequestParam("flag") Integer flag) {
        return webClientBuilder.build().get().uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8888)
                        .path("/api/test")
                        .queryParam("flag", flag).build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new CustomException(new RestVo(400, "Bad Request"))))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new CustomException(new RestVo(500, "Server Error"))))
                .toEntity(String.class)
                .map(responseEntity -> new RestVo(responseEntity.getStatusCode().value(), responseEntity.getBody()))
                .onErrorResume(CustomException.class, ex -> Mono.just(ex.getRestVo()));
    }


}

@Data
@AllArgsConstructor
class RestVo {
    private Integer code;
    private String message;
}

class CustomException extends RuntimeException {
    private final RestVo restVo;

    public CustomException(RestVo restVo) {
        this.restVo = restVo;
    }

    public RestVo getRestVo() {
        return restVo;
    }
}