package io.github.susimsek.account.cache;

import java.util.Collections;
import java.util.List;
public interface CacheDefaults {

      interface Redis {
            List<String> server = Collections.singletonList("redis://localhost:6379");
            int expiration = 300; // 5 minutes
            boolean cluster = false;
            int connectionPoolSize = 64; // default as in redisson
            int connectionMinimumIdleSize = 24; // default as in redisson
            int subscriptionConnectionPoolSize = 50; // default as in redisson
            int subscriptionConnectionMinimumIdleSize = 1; // default as in redisson
            String username = "default"; // 5 minutes
            long dnsMonitoringInterval = 5000L;
      }
}