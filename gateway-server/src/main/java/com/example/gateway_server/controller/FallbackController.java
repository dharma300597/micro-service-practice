package com.example.gateway_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/fall-back")
public class FallbackController {

    @GetMapping("service-failure")
    public Mono<String> serviceUnavailableFallbackResponse(){
        return  Mono.just("Service unreachable.please contact support team");
    }
}
