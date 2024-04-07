package io.github.susimsek.account.config;

import io.github.susimsek.account.logging.webclient.LoggingExchangeFilterFunction;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HttpClientProperties.class)
public class WebClientConfig {

    @Bean
    public HttpClient httpClient(HttpClientProperties httpClientProperties) {
        var config = httpClientProperties.getConfig();
        return HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout())
            .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(config.getReadTimeout() / 1000))
                .addHandlerLast(new WriteTimeoutHandler(config.getWriteTimeout() / 1000)));
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder(
        HttpClient httpClient,
        LoggingExchangeFilterFunction loggingExchangeFilterFunction) {
        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .filter(loggingExchangeFilterFunction);
    }
}
