package com.sean.gatewaydemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
public class WebSocketConfig {

    private final EchoHandler echoHandler;

    public WebSocketConfig(EchoHandler echoHandler) {
        this.echoHandler = echoHandler;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, Object> map = Map.of(
                "/websocket/echo", echoHandler
        );

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        // 设置映射的优先级
        mapping.setOrder(-1);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
