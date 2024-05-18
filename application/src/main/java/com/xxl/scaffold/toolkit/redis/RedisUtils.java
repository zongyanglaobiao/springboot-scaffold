package com.xxl.scaffold.toolkit.redis;

import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 封装redis工具类
 * @author xxl
 * @since 2023/11/10
 */
@Component
public class RedisUtils {

    private  RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /*========================================*/
    /*===============common===================*/
    /*========================================*/

    /**
     * 查看所有KEY
     *
     * @return Set<String>
     */
    public Set<String> keys() {
        return redisTemplate.keys("*");
    }

    /**
     * 获取 redisTemplate
     *
     * @return redisTemplate
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 移动某个键到指定redis数据库
     *
     * @param key 键
     * @param dbIndex 数据库下标
     * @return boolean
     */
    public boolean move(String key,int dbIndex) {
        return Boolean.TRUE.equals(redisTemplate.move(key, dbIndex));
    }

    /**
     * 查看是否有这个键
     *
     * @param key 键
     * @return boolean
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除某个键
     *
     * @param key 键
     * @return Long
     */
    public Long delete(String ...key) {
        return redisTemplate.delete(List.of(key));
    }

    /**
     * 获取RedisConnection
     *
     * @return RedisConnection
     */
    public  RedisConnection getConnection() {
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        return  connectionFactory != null ? connectionFactory.getConnection() : null;
    }

    /**
     * 获取RedisCommands操作redis底层
     *
     * @param function 函数
     * @param <T> 指定类型
     * @return T
     */
    public <T> T getCommands(Function<RedisCommands,T> function) {
        RedisConnection connection = this.getConnection();
        Assert.notNull(connection,"connection is null");
        T result = function.apply(connection.commands());
        connection.close();
        return result;
    }

    /**
     * 查看数据库大小
     *
     * @return Long
     */
    public Long dbSize() {
        return this.getCommands(RedisServerCommands::dbSize);
    }

    /**
     * 谋取某个键的过期时间
     *
     * @param key 键
     * @return  Long
     */
    public Long getExpire(String key) {
       return   redisTemplate.getExpire(key);
    }

    /**
     * 查看某个键的类型
     *
     * @param key 键
     * @return DataType
     */
    public DataType type(String key) {
        return   redisTemplate.type(key);
    }

    /**
     * 移除某个键的过期时间
     *
     * @param key 键
     * @return boolean
     */
    public boolean persist(String key) {
        return Boolean.TRUE.equals(redisTemplate.persist(key));
    }

    /**
     * 给某个键设置过期时间
     *
     * @param key 键
     * @param time 时间
     * @return boolean
     */
    public boolean expire(String key,long time) {
        return expireAndTimeunit(key,time,TimeUnit.SECONDS);
    }

    /**
     * 给某个键设置过期时间
     *
     * @param key 键
     * @param time 时间
     * @param timeUnit 时间单位
     * @return boolean
     */
    public boolean expireAndTimeunit(String key,long time,TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, time, timeUnit));
    }
    /*========================================*/
    /*===============String===================*/
    /*========================================*/

    /**
     * 存储
     *
     * @param key 键
     * @param value  值
     */
    public void opsForValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 存储并且施加时间
     *
     * @param key 键
     * @param value 值
     * @param time  时间
     */
    public void opsForValue(String key, Object value,long time) {
        this.opsForValue(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 存储并且施加时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit  时间单位
     */
    public void opsForValue(String key, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 通过键获取某个值
     *
     * @param key 键
     * @return  Object
     */
    public <E> E get(String key,Class<E> cls) {
        Object object = redisTemplate.opsForValue().get(key);
        return Convert.convert(cls,object);
    }

    /**
     * 通过键获取某个值，并且施加值
     *
     * @param key 键
     * @param time 时间
     * @return  Object
     */
    public Object getAndExpire(String key, long time) {
        return redisTemplate.opsForValue().getAndExpire(key,time,TimeUnit.SECONDS);
    }

    /**
     * 通过键获取某个值，并且施加值
     *
     * @param key 键
     * @param time 时间
     * @param timeUtil 时间单位
     * @return  Object
     */
    public Object getAndExpire(String key, long time, TimeUnit timeUtil) {
        return redisTemplate.opsForValue().getAndExpire(key,time,timeUtil);
    }

}
