package com.xxl.scaffold;

import com.xxl.sdk.log.AsyncLogger;
import com.xxl.sdk.util.thead.TheadUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Indexed;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: anyone
 * @since: 2023/9/16
 * @description:  启动类
 */
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@Slf4j
@Indexed
@MapperScan("**.mapper")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
            log.info("项目启动成功(ง ˙o˙)ว");
        } catch (Exception e) {
            log.error("启动失败:",e);
        }
    }

    @Bean
    public AsyncLogger logger() {
        return new AsyncLogger(TheadUtils.createThreadPool());
    }
}