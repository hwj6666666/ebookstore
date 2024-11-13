package org.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }

  //  @Bean
  //  public RestClient.Builder restClientBuilder() {
  //    return RestClient.builder();
  //  }

  @Bean
  public RouteLocator myRoutes(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(r -> r.path("/author/**").uri("lb://FETCH-AUTHOR"))
        .route(r -> r.path("/calculator/**").uri("lb://CALCULATOR"))
        .route(r -> r.path("/**").uri("lb://BOOKSTORE-BACKEND"))
        .build();
  }
}
