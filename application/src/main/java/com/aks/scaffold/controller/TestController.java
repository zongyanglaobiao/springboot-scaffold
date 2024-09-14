package com.aks.scaffold.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.aks.scaffold.controller.entity.UserEntity;
import com.aks.scaffold.controller.mapper.UserMapper;
import com.aks.sdk.resp.RespEntity;
import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 测试项目功能可以删除
 * @author xxl
 * @since 2023/9/16
 */
@RestController
@RequestMapping(value = "/test", produces = "application/json")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "测试API")
public class TestController {

    private final RedisTemplate<String, Object> redisTemplate;

    private final TransactionTemplate transactionTemplate;

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 测试接口
     */
    @GetMapping("/hello")
    @Operation(summary = "测试接口")
    public RespEntity<String> hello() {
        System.out.println(1 / 0);
        return RespEntity.success();
    }

    /**
     * 登录接口
     */
    @GetMapping("/login")
    @Operation(summary = "测试登录")
    public RespEntity<Object> login() {
        StpUtil.login(1);
        return RespEntity.success(Map.of("tokenName",StpUtil.getTokenName(),"tokenValue",StpUtil.getTokenValue()));
    }

    @GetMapping("/batch")
    @Operation(summary = "测试批量插入")
    public RespEntity<List<UserEntity>> batch() {
        MybatisBatch.Method<UserEntity> mapperMethod = new MybatisBatch.Method<>(UserMapper.class);
        // 执行批量插入
        MybatisBatchUtils.execute(sqlSessionFactory, UserEntity.mock(), mapperMethod.insert());
        return RespEntity.success(Db.list(UserEntity.class));
    }


    /**
     * 测试redis
     */
    @GetMapping("redis")
    @Operation(summary = "测试redis")
    @Parameter(name = "key",description = "redis key")
    public RespEntity<String> redis(@RequestParam("key")String key) {
        redisTemplate.opsForValue().set("username","xxl");
        Object o = redisTemplate.opsForValue().get(key);
        log.info("查询: key=>{},value=>{}",key,o);
        redisTemplate.keys("*").forEach(dto -> log.info("key=>{},value=>{}",dto,redisTemplate.opsForValue().get(dto)));
        return RespEntity.success();
    }





}
