package com.sean.gatewaydemo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class EchoHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(final WebSocketSession session) {
        AtomicReference<Disposable> subscription = new AtomicReference<>();

        return session.receive()
                .flatMap(message -> {

                    Disposable oldSubscription = subscription.getAndSet(null);
                    if (oldSubscription != null)
                        oldSubscription.dispose();

                    String payload = message.getPayloadAsText();

                    Disposable disposable = Flux.interval(Duration.ofSeconds(1))
                            .map(seq -> session.textMessage(payload + "-" + seq))
                            .flatMap(msg -> session.send(Mono.just(msg)))
                            .subscribe();

                    subscription.set(disposable);

                    return Mono.empty();  // Return an empty Mono to keep processing chain alive
                }).then();
    }
}