package com.example.gateway_server;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JAutoConfiguration;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import org.springframework.cloud.client.circuitbreaker.Customizer;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootApplication
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

    @Bean
    public RouteLocator customRouterConfig(RouteLocatorBuilder routeLocatorBuilder){
        return  routeLocatorBuilder.routes()
                .route(p-> p.path("/bank/accounts/**")
                        .filters(f-> f.rewritePath("/bank/accounts/?(?<segment>.*)","/${segment}")
                                .circuitBreaker(config -> config.setName("accountCircuitBreaker")
                                        .setFallbackUri("forward:/api/fall-back/service-failure"))
                        )
                        .uri("lb://ACCOUNTS"))
                .route(p-> p.path("/bank/cards/**")
                        //below we have given retry logic and backoff logic .in that backoff logic  we will give delay time,max delay time , factor which time should the time get incremented like (initial delay 100,factor 2 -> retry 1 100 : retry 2 2*100 : retry 3 200*4 , then last value is  basedOnPreviousValue which decides whether to take previous delay time or not )
                        .filters(f-> f.rewritePath("/bank/cards/?(?<segment>.*)","/${segment}").retry(routConfig-> routConfig.setRetries(3).setMethods(HttpMethod.GET).setBackoff(Duration.ofMillis(300),Duration.ofMillis(1000),3,true)))
                        .uri("lb://CARDS"))
                .route(p-> p.path("/bank/loans/**")
                        .filters(f-> f.rewritePath("/bank/loans/?(?<segment>.*)","/${segment}").requestRateLimiter(config-> config.setRateLimiter(redisRateLimiter()).setKeyResolver(userkeyResolver())))
                        .uri("lb://LOANS"))
                .build();
    }

    //Below bean is to  override custom value of circuit breaker .in that we haven't changed all value only increased the response wait time
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> getCustomCircuitBreakerConfig(){
        return factory-> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
    }

    //Below  configuration is to create ratelimiter configuration through java if you don't need to like this you can also give in application.yml or properties
    @Bean
    public RedisRateLimiter redisRateLimiter(){
        //While creating the rate limiter we need to  give replenish rate ,burst capacity, request token
        return new RedisRateLimiter(1,1,1);
    }
    //Key resolver is mandatory while implementing  rate limiting,it groups the request by fetching certain details from the header and implement's the rate limiting logic to that group
    @Bean
    public KeyResolver userkeyResolver(){
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user")).defaultIfEmpty("anonymous");
    }
}
