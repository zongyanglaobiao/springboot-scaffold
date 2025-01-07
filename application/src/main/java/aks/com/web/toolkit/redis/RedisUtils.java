package aks.com.web.toolkit.redis;

import cn.hutool.core.convert.Convert;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 封装redis工具类
 * @author xxl
 * @since 2023/11/10
 */
public class RedisUtils {
    /**
     * -- GETTER --
     *  获取 redisTemplate
     */
    private static RedisTemplate<String, Object> redisTemplate;

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static RedisTemplate<String, Object> getRedisTemplate() {
        if (Objects.isNull(redisTemplate)) {
            throw new RuntimeException("redisTemplate is null");
        }
        return RedisUtils.redisTemplate;
    }

    /*========================================*/
    /*===============common===================*/
    /*========================================*/

    /**
     * 查看所有KEY
     *
     * @return Set<String>
     */
    public static Set<String> keys() {
        return getRedisTemplate().keys("*");
    }

    /**
     * 移动某个键到指定redis数据库
     *
     * @param key 键
     * @param dbIndex 数据库下标
     * @return boolean
     */
    public static boolean move(String key,int dbIndex) {
        return Boolean.TRUE.equals(getRedisTemplate().move(key, dbIndex));
    }

    /**
     * 查看是否有这个键
     *
     * @param key 键
     * @return boolean
     */
    public static boolean hasKey(String key) {
        return Boolean.TRUE.equals(getRedisTemplate().hasKey(key));
    }

    /**
     * 删除某个键
     *
     * @param key 键
     * @return Long
     */
    public static Long delete(String ...key) {
        return getRedisTemplate().delete(List.of(key));
    }

    /**
     * 获取RedisConnection
     *
     * @return RedisConnection
     */
    public static RedisConnection getConnection() {
        RedisConnectionFactory connectionFactory = getRedisTemplate().getConnectionFactory();
        return  connectionFactory != null ? connectionFactory.getConnection() : null;
    }

    /**
     * 获取RedisCommands操作redis底层
     *
     * @param function 函数
     * @param <T> 指定类型
     * @return T
     */
    public static <T> T getCommands(Function<RedisCommands,T> function) {
        RedisConnection connection = getConnection();
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
    public static Long dbSize() {
        return getCommands(RedisServerCommands::dbSize);
    }

    /**
     * 谋取某个键的过期时间
     *
     * @param key 键
     * @return  Long
     */
    public static Long getExpire(String key) {
       return  getRedisTemplate().getExpire(key);
    }

    /**
     * 查看某个键的类型
     *
     * @param key 键
     * @return DataType
     */
    public static DataType type(String key) {
        return getRedisTemplate().type(key);
    }

    /**
     * 移除某个键的过期时间
     *
     * @param key 键
     * @return boolean
     */
    public static boolean persist(String key) {
        return Boolean.TRUE.equals(getRedisTemplate().persist(key));
    }

    /**
     * 给某个键设置过期时间
     *
     * @param key 键
     * @param time 时间
     * @return boolean
     */
    public static boolean expire(String key,long time) {
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
    public static boolean expireAndTimeunit(String key,long time,TimeUnit timeUnit) {
        return Boolean.TRUE.equals(getRedisTemplate().expire(key, time, timeUnit));
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
    public static void set(String key, Object value) {
        getRedisTemplate().opsForValue().set(key, value);
    }

    /**
     * 存储并且施加时间
     *
     * @param key 键
     * @param value 值
     * @param time  时间
     */
    public static void set(String key, Object value,long time) {
        set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 存储并且施加时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit  时间单位
     */
    public static void set(String key, Object value, long time, TimeUnit timeUnit) {
        getRedisTemplate().opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 通过键获取某个值
     *
     * @param key 键
     * @return  Object
     */
    public static Object get(String key) {
        return getRedisTemplate().opsForValue().get(key);
    }

    /**
     * 通过键获取某个值
     *
     * @param key 键
     * @return  Object
     */
    public static <E> E get(String key,Class<E> cls) {
        Object object = getRedisTemplate().opsForValue().get(key);
        return Convert.convert(cls,object);
    }

    /**
     * 通过键获取某个值，并且施加值
     *
     * @param key 键
     * @param time 时间
     * @return  Object
     */
    public static Object getAndExpire(String key, long time) {
        return getRedisTemplate().opsForValue().getAndExpire(key,time,TimeUnit.SECONDS);
    }

    /**
     * 通过键获取某个值，并且施加值
     *
     * @param key 键
     * @param time 时间
     * @param timeUtil 时间单位
     * @return  Object
     */
    public static Object getAndExpire(String key, long time, TimeUnit timeUtil) {
        return getRedisTemplate().opsForValue().getAndExpire(key,time,timeUtil);
    }

}
