package com.jjh.framework.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjh.common.key.CacheConstants;
import com.jjh.framework.context.TenantContextHolder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringBoot2.0整合SpringCache和Redis
 * https://blog.csdn.net/moshowgame/article/details/80792774
 *
 * @author jjh
 * @date 2019/9/16
 **/
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * RedisTemplate配置
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        //Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        //使用Fastjson2JsonRedisSerializer来序列化和反序列化redis的value值 by zhengkai
        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 缓存配置管理器
     * 配置@Cacheable注解的缓存过期时间
     * 使用fastjson作为缓存对象序列化工具，避免与springmvc的jackson发生注解冲突
     * TTL设置：https://cloud.tencent.com/developer/article/1497599
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory) {
        // 配置序列化（缓存默认有效期 6小时）
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                // 添加租户缓存隔离（添加redis缓存前缀）
                .computePrefixWith(cacheName -> TenantContextHolder.getTenantRedisKey()+ cacheName)
                .entryTtl(Duration.ofHours(6)).disableCachingNullValues();
        RedisCacheConfiguration redisCacheConfiguration = defaultConfig.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new FastJson2JsonRedisSerializer(Object.class)));

        // 无过期时间
        RedisCacheConfiguration zeroConfig = RedisCacheConfiguration.defaultCacheConfig()
                // 添加租户缓存隔离 （添加redis缓存前缀）
                .computePrefixWith(cacheName -> TenantContextHolder.getTenantRedisKey()+ cacheName)
                .entryTtl(Duration.ZERO).disableCachingNullValues().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new FastJson2JsonRedisSerializer(Object.class)));


        // 配置系统相关缓存无过期时间
        Map<String, RedisCacheConfiguration> initialCacheConfiguration = new HashMap<String, RedisCacheConfiguration>(){{
            put(CacheConstants.SYS_BASE_DICT_PROVINCIAL_CODE, zeroConfig);
            put(CacheConstants.SYS_BASE_DICT_PROVINCIAL_CODESTR, zeroConfig);
        }};

        // 创建默认缓存配置对象
        RedisCacheManager cacheManager = RedisCacheManager.builder(RedisCacheWriter.lockingRedisCacheWriter(factory)).cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(initialCacheConfiguration)
                .transactionAware().build();
        return cacheManager;
    }

}
