package com.example.gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

    @Bean
    public RouteLocator customRouterConfig(RouteLocatorBuilder routeLocatorBuilder){
        return  routeLocatorBuilder.routes()
                .route(p-> p.path("/bank/accounts/**")
                        .filters(f-> f.rewritePath("/bank/accounts/?(?<segment>.*)","/${segment}"))
                        .uri("lb://ACCOUNTS"))
                .route(p-> p.path("/bank/cards/**")
                        .filters(f-> f.rewritePath("/bank/cards/?(?<segment>.*)","/${segment}"))
                        .uri("lb://CARDS"))
                .route(p-> p.path("/bank/loans/**")
                        .filters(f-> f.rewritePath("/bank/loans/?(?<segment>.*)","/${segment}"))
                        .uri("lb://LOANS"))
                .build();
    }

}
