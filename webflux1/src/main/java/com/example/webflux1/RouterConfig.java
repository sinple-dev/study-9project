package com.example.webflux1;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final SampleHandler sampleHandler;
    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route()
                .GET("/test1", sampleHandler::getString)
                .build();
    }
}
