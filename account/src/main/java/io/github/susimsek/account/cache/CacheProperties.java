package io.github.susimsek.account.cache;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

@ConfigurationProperties("cache")
@Getter
@Setter
public class CacheProperties {

    @NestedConfigurationProperty
    private final Redis redis = new Redis();

    @Getter
    @Setter
    public static class Redis {
        private List<String> server = CacheDefaults.Redis.server;
        private int expiration = CacheDefaults.Redis.expiration;
        private boolean cluster = CacheDefaults.Redis.cluster;
        private int connectionPoolSize = CacheDefaults.Redis.connectionPoolSize;
        private int connectionMinimumIdleSize = CacheDefaults.Redis.connectionMinimumIdleSize;
        private int subscriptionConnectionPoolSize = CacheDefaults.Redis.subscriptionConnectionPoolSize;
        private int subscriptionConnectionMinimumIdleSize = CacheDefaults.Redis.subscriptionConnectionMinimumIdleSize;
        private String username = CacheDefaults.Redis.username;
        private String password;
        private long dnsMonitoringInterval = CacheDefaults.Redis.dnsMonitoringInterval;
    }
}
