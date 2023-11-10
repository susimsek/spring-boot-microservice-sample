package io.github.susimsek.account.config;


import io.github.susimsek.account.cache.CacheProperties;
import lombok.val;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.cfg.AvailableSettings;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SnappyCodecV2;
import org.redisson.config.Config;
import org.redisson.hibernate.RedissonRegionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Map;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(CacheProperties cacheProperties) {
        var config = new Config();
        if (cacheProperties.getRedis().isCluster()) {
            val clusterServers = config
                .useClusterServers()
                .setMasterConnectionPoolSize(cacheProperties.getRedis().getConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(
                    cacheProperties.getRedis().getConnectionMinimumIdleSize()
                )
                .setSubscriptionConnectionPoolSize(
                    cacheProperties.getRedis().getSubscriptionConnectionPoolSize()
                )
                .addNodeAddress(cacheProperties.getRedis().getServer().toArray(String[]::new))
                .setDnsMonitoringInterval(cacheProperties.getRedis().getDnsMonitoringInterval())
                .setUsername(cacheProperties.getRedis().getUsername());
            if (StringUtils.hasText(cacheProperties.getRedis().getPassword())) {
                clusterServers.setPassword(cacheProperties.getRedis().getPassword());
            }
        } else {
            val singleServer = config
                .useSingleServer()
                .setConnectionPoolSize(cacheProperties.getRedis().getConnectionPoolSize())
                .setConnectionMinimumIdleSize(cacheProperties.getRedis().getConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(
                    cacheProperties.getRedis().getSubscriptionConnectionPoolSize()
                )
                .setAddress(cacheProperties.getRedis().getServer().get(0))
                .setDnsMonitoringInterval(cacheProperties.getRedis().getDnsMonitoringInterval())
                .setUsername(cacheProperties.getRedis().getUsername());
            if (StringUtils.hasText(cacheProperties.getRedis().getPassword())) {
                singleServer.setPassword(cacheProperties.getRedis().getPassword());
            }
        }
        config.setCodec(new SnappyCodecV2());
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache")
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(RedissonClient redissonClient) {
        return hibernateProperties -> hibernateProperties.put(AvailableSettings.CACHE_REGION_FACTORY, new RedissonRegionFactory() {
            @Override
            protected RedissonClient createRedissonClient(StandardServiceRegistry registry, Map properties) {
                return redissonClient;
            }
        });
    }
}
