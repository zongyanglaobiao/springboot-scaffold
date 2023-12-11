package com.template.controller;

import com.template.core.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试项目是否可以运行
 * @author xxl
 * @since 2023/9/16
 */
@RestController
@RequestMapping(value = "/test", produces = "application/json")
@Slf4j
@Tag(name = "测试API")
public class TestController {
    /**
     * 测试接口
     * @return
     */
    @GetMapping("/hello")
    @Operation(summary = "测试接口")
    public RespEntity<String> hello() {
        return RespEntity.base(200,"success",null);
    }

    @Resource
    RedisTemplate<String, Object> redisTemplate;
    /**
     * 测试redis
     * @param key
     * @return 成功返回OK
     */
    @GetMapping("redis")
    @Operation(summary = "测试redis")
    @Parameter(name = "key",description = "redis的key")
    public RespEntity<String> redis(@RequestParam("key")String key) {
        redisTemplate.opsForValue().set("username","xxl");
        Object o = redisTemplate.opsForValue().get(key);
        log.info("查询：key=>{},value=>{}",key,o);
        redisTemplate.keys("*").forEach(dto -> log.info("key=>{},value=>{}",dto,redisTemplate.opsForValue().get(dto)));
        return RespEntity.base(200,"success",null);
    }
}
