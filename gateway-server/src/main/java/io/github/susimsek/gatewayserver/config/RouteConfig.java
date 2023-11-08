package io.github.susimsek.gatewayserver.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Configuration(proxyBeanMethods = false)
public class RouteConfig {

    @Bean
    public RouteLocator eazyBankRouteConfig(
        RouteLocatorBuilder routeLocatorBuilder,
        KeyResolver userKeyResolver,
        RedisRateLimiter redisRateLimiter
        ) {
        return routeLocatorBuilder.routes()
            .route(p -> p
                .path("/eazybank/account/**")
                .filters(f -> f.rewritePath("/eazybank/account/(?<segment>.*)", "/${segment}")
                    .addResponseHeader("X-Response-Time", Instant.now().toString())
                    .circuitBreaker(config -> config.setName("accountCircuitBreaker")
                        .setFallbackUri("forward:/contactSupport")))
                .uri("lb://account"))
            .route(p -> p
                .path("/eazybank/loan/**")
                .filters(f -> f.rewritePath("/eazybank/loan/(?<segment>.*)", "/${segment}")
                    .addResponseHeader("X-Response-Time", Instant.now().toString())
                    .retry(retryConfig -> retryConfig.setRetries(3)
                        .setMethods(HttpMethod.GET)
                        .setBackoff(
                            Duration.ofMillis(100),
                            Duration.ofMillis(1000),
                            2,true)))
                .uri("lb://loan"))
            .route(p -> p
                .path("/eazybank/card/**")
                .filters(f -> f.rewritePath("/eazybank/card/(?<segment>.*)", "/${segment}")
                    .addResponseHeader("X-Response-Time", Instant.now().toString())
                    .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter)
                        .setKeyResolver(userKeyResolver)))
                .uri("lb://card"))
            .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 1, 1);
    }

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
            .defaultIfEmpty("anonymous");
    }

}
