package com.pm.apigateway;

import com.pm.apigateway.Filters.JwtValidationFilterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ApiGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGateWayApplication.class, args);
    }
    @Bean
    public RouteLocator patientRoutMapping(RouteLocatorBuilder builder, JwtValidationFilterFactory jwtFilterFactory) {
        return builder.routes()
                .route(p->p.path("/api/auth/**")
                        .filters(f->f.rewritePath("/api/auth(?<segment>/.*)","${segment}"))
                        .uri("http://Auth-Service-Continer:4005")
                )


                .route(p -> p.path("/api/Patients/**")
                        .filters(f -> f
                                .rewritePath("/api/Patients(?<segment>/.*)", "/Patients${segment}")
                                .filter(jwtFilterFactory.apply(new Object()))
                        )
                        .uri("http://Patinet-service-container:4000")
                )
                .build();

    }



}
