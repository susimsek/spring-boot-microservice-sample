package io.github.susimsek.card.config;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.Map;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.cfg.AvailableSettings;
import org.redisson.api.RedissonClient;
import org.redisson.hibernate.RedissonRegionFactory;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RedissonClient.class})
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {

    @Bean
    @ConditionalOnProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache")
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(RedissonClient redissonClient) {
        return hibernateProperties ->
            hibernateProperties.put(AvailableSettings.CACHE_REGION_FACTORY, new RedissonRegionFactory() {
                @Override
                protected RedissonClient createRedissonClient(StandardServiceRegistry registry, Map properties) {
                    return redissonClient;
                }
            });
    }

    @Bean
    public CacheManager cacheManager(
        RedissonClient redissonClient,
        CacheProperties cacheProperties) {
        Map<String, org.redisson.spring.cache.CacheConfig> config = new HashMap<>();
        var regions = cacheProperties.getRedisson().getRegions();
        if (!regions.isEmpty()) {
            config = regions.entrySet().stream().collect(
                toMap(
                    Map.Entry::getKey,
                    entry -> createCache(entry.getValue()),
                    (first, second) -> first));
        }
        return new RedissonSpringCacheManager(redissonClient, config);
    }

    private org.redisson.spring.cache.CacheConfig createCache(
        CacheProperties.Redisson.Region region) {
        var expiration = region.getExpiration();
        return new org.redisson.spring.cache.CacheConfig(
            expiration.getTimeToLive(), expiration.getMaxIdleTime());
    }
}
