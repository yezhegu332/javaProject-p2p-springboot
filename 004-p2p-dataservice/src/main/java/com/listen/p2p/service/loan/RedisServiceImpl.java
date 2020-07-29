package com.listen.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.listen.p2p.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Listen
 * @Date: 2020/7/28
 */
@Component
@Service(interfaceClass = RedisService.class,version = "1.0.0",timeout = 150000)
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value,60, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, String value,Long time,TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value,time, timeUnit);
    }

    @Override
    public String get(String key) {
        return (String)redisTemplate.opsForValue().get(key);
    }
}
