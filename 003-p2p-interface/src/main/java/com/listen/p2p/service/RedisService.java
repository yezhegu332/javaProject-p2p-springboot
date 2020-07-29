package com.listen.p2p.service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Listen
 * @Date: 2020/7/28
 */
public interface RedisService {

    void set(String key,String value);

    void set(String key, String value, Long time, TimeUnit timeUnit);

    String get(String key);

}
