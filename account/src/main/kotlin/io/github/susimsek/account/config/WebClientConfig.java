package io.github.susimsek.account.config;

import io.github.susimsek.account.logging.webclient.LoggingExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder(LoggingExchangeFilterFunction loggingExchangeFilterFunction) {
        return WebClient.builder()
            .filter(loggingExchangeFilterFunction);
    }
}
