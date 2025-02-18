package com.project.cheerha.common.config;

import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer()
//                .setAddress("redis://redis:6379");  //도커로 실행할 경우
                .setAddress("redis://localhost:6379");    //로컬에서 실행할 경우
//                .setAddress("rediss://master.cheerha-redis.tlrpjs.apn2.cache.amazonaws.com:6379");   //aws
        return org.redisson.Redisson.create(config);
    }
}
