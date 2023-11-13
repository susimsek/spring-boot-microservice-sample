package io.github.susimsek.card.config;


import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.cfg.AvailableSettings;
import org.redisson.api.RedissonClient;
import org.redisson.hibernate.RedissonRegionFactory;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RedissonClient.class})
@EnableCaching
public class CacheConfig {

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

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, org.redisson.spring.cache.CacheConfig> config = new HashMap<>();

        // create "testMap" spring cache with ttl = 24 minutes and maxIdleTime = 12 minutes
        config.put("testMap", new org.redisson.spring.cache.CacheConfig(24*60*1000, 12*60*1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
