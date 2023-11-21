package io.github.susimsek.configserver.config;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpringSecurityDefaults {

    public static final String CONTENT_SECURITY_POLICY = "script-src 'self'";
}
