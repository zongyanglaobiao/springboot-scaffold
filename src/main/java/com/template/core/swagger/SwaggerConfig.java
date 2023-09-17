package com.template.core.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * knife4j的配置
 * @author xxl
 * @since 2023/9/16
 */
@OpenAPIDefinition(
        info = @Info(
                title = "接口文档",description = "接口文档",version = "version:1.0.0",
                contact = @Contact(name = "XXL",email = "3578144921@qq.com")
        ),
        servers = {@Server(url = "http://localhost:8080/",description = "本地环境")}
)
@Configuration
public class SwaggerConfig {

}
