package com.example.demo.common;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class PreGatewayAuthorizationFilterFactory extends AbstractGatewayFilterFactory<PreGatewayAuthorizationFilterFactory.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            return chain.filter(exchange);
        };
    }


    public static class Config {
    }

}