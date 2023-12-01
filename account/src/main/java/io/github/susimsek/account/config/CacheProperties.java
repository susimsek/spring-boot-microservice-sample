package io.github.susimsek.account.config;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.cache")
@Getter
@Setter
public class CacheProperties {

    private Redisson redisson = new Redisson();

    @Getter
    @Setter
    public static class Redisson {
        private Map<String, Region> regions;

        @Getter
        @Setter
        public static class Region {
            private Expiration expiration;

            @Getter
            @Setter
            public static class Expiration {
                private long timeToLive;
                private long maxIdleTime;
            }
        }
    }
}
