package io.github.susimsek.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("httpclient")
@Getter
@Setter
public class HttpClientProperties {

    private Config config = new Config();

    @Getter
    @Setter
    public static class Config {
        private int connectTimeout = HttpClientDefaults.Config.CONNECT_TIMEOUT;
        private int readTimeout = HttpClientDefaults.Config.READ_TIMEOUT;
        private int writeTimeout = HttpClientDefaults.Config.WRITE_TIMEOUT;
    }
}
