package com.sean.gatewaydemo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class EchoHandler implements WebSocketHandler {

    private Map<String, List<WebSocketSession>> userSessionsMap = new ConcurrentHashMap<>();
    private Map<String, Flux<String>> userFluxMap = new ConcurrentHashMap<>();

    private String getUserId(WebSocketSession session) {
        String query = session.getHandshakeInfo().getUri().getQuery();
        return Arrays.stream(query.split("&"))
                .map(param -> param.split("="))
                .filter(keyValue -> keyValue[0].equals("userId"))
                .findFirst()
                .map(keyValue -> keyValue[1]).orElse("defaultUser");
    }

    private Flux<String> createFlux() {
        Flux<Long> seed = Flux.interval(Duration.ofSeconds(1));
        return seed.map(seq -> "msg-" + seq).share();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String userId = getUserId(session);
        userSessionsMap.computeIfAbsent(userId, k -> Collections.synchronizedList(new ArrayList<>())).add(session);

        Flux<String> flux = userFluxMap.computeIfAbsent(userId, u -> createFlux());
        return session.send(flux.map(session::textMessage))
                .doFinally(signal -> {
                    List<WebSocketSession> sessions = userSessionsMap.get(userId);
                    sessions.remove(session);
                    if (sessions.isEmpty()) {
                        userSessionsMap.remove(userId);
                        userFluxMap.remove(userId);
                    }
                });
    }
}