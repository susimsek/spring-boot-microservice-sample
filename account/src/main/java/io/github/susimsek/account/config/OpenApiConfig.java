package io.github.susimsek.account.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Configuration(proxyBeanMethods = false)
@OpenAPIDefinition(
    info = @Info(
        title = "Account microservice REST API Documentation",
        description = "EazyBank Account microservice REST API Documentation",
        version = "v1",
        contact = @Contact(
            name = "Şuayb Şimşek",
            email = "suaybsimsek58@gmail.com",
            url = "https://www.susimsek.github.io"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    externalDocs = @ExternalDocumentation(
        description =  "EazyBank Account microservice REST API Documentation",
        url = "https://www.susimsek.github.io/swagger-ui.html"
    )
)
public class OpenApiConfig {

}
