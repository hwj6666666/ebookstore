package org.example.gateway.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  @Bean
  public CorsWebFilter corsWebFilter() {
    CorsConfiguration config = new CorsConfiguration();
    //    config.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 允许特定来源
    config.setAllowedOriginPatterns(Collections.singletonList("*")); // 允许所有来源
    config.addAllowedMethod("*"); // 允许所有方法
    config.addAllowedHeader("*"); // 允许所有头部
    config.setAllowCredentials(true); // 允许凭证

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsWebFilter(source);
  }
}
