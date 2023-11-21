package io.github.susimsek.configserver.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.security")
@Getter
@Setter
public class SpringSecurityProperties {

    private User user = new User();
    private String contentSecurityPolicy = SpringSecurityDefaults.CONTENT_SECURITY_POLICY;

    @Getter
    @Setter
    public static class User {
        private String name;
        private String password;
        private List<String> roles = new ArrayList<>();
    }
}
