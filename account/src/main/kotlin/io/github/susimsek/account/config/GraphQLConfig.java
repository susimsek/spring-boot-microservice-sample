package io.github.susimsek.account.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.GraphQlClientInterceptor;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class GraphQLConfig {

    @Bean
    public GraphQLScalarType dateTimeScalarType() {
        return ExtendedScalars.DateTime;
    }

    @Bean
    public RuntimeWiringConfigurer graphqlConfigurer(
        List<GraphQLScalarType> graphQLScalarTypes
    ) {
        return builder -> {
            graphQLScalarTypes.forEach(
                builder::scalar);
        };
    }

    @Bean
    public HttpGraphQlClient.Builder<?> httpGraphQlClient(
        WebClient.Builder  loadBalancedWebClientBuilder,
        GraphQlClientInterceptor graphqlLoggingInterceptor) {
        return HttpGraphQlClient.builder(loadBalancedWebClientBuilder)
            .interceptor(graphqlLoggingInterceptor);
    }
}
