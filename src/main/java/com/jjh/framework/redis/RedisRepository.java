package com.jjh.framework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * redis操作服务
 *
 * @author jjh
 * @date 2019/11/29
 **/
@Service
public class RedisRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /* 队列操作 */
    /**
     * 入队
     * @param key   键
     * @param val   值
     * @return
     * @throws Exception
     */
    public Long lpush(String key, Object val) {
        return redisTemplate.opsForList().leftPush(key, val);
    }

    /**
     * 出队
     * @param key   键
     * @return
     * @throws Exception
     */
    public Object rpop(String key)  {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 存入对象到redis
     * @param key   索引
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 删除对象
     * @param key   索引
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除对象
     * @param prefix   前缀
     * @param key   索引
     */
    public void delete(String prefix, String key) {
        this.delete(prefix +":"+ key);
    }

    /**
     * 存入对象到redis
     * @param key   索引
     * @param value 值
     * @param timeout 过期时间
     * @param unit 过期时间单位（TimeUnit）
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 存入对象到redis
     * @param prefix   索引前缀
     * @param key   索引
     * @param value 值
     * @param timeout 过期时间
     * @param unit 过期时间单位（TimeUnit）
     */
    public void set(String prefix, String key, Object value, long timeout, TimeUnit unit) {
        this.set(prefix +":"+ key, value, timeout, unit);
    }

    /**
     * 存入对象到redis
     * @param prefix   索引前缀
     * @param key   索引
     * @param value 值
     */
    public void set(String prefix, String key, Object value) {
        redisTemplate.opsForValue().set(prefix +":"+ key, value);
    }

    /**
     * 获取对象
     * @param prefix   索引前缀
     * @param key   索引
     */
    public Object get(String prefix, String key) {
        return this.get(prefix +":"+ key);
    }

    /**
     * 获取对象
     * @param key   索引
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
