package com.sean.gatewaydemo;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class OrderServiceFilter implements GatewayFilterFactory<OrderServiceFilter.Config> {

    public static class Config {
        // 这里可以添加你的配置属性，如果没有则可以留空
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("client-id", "your-client-id")
                    .build();
            ServerWebExchange newExchange = exchange.mutate().request(request).build();
            return chain.filter(newExchange)
                    .then(Mono.fromRunnable(() -> {
                        HttpStatusCode httpStatus = newExchange.getResponse().getStatusCode();
                        System.out.println("Response HTTP Status: " + httpStatus);
                    }));
        };
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public Config newConfig() {
        return new Config();
    }
}