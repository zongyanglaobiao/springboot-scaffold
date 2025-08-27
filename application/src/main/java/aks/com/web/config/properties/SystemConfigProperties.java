package aks.com.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.stream.Stream;

/**
 * 系统自定义配置,都应该放在这个配置文件中
 * @author jamesaks
 * @since 2025/8/4
 */
@ConfigurationProperties("system.config")
@Data
public class SystemConfigProperties {

    private Authorization authorization;

    @Data
    public static class Authorization{
        /**
         *  拦截路径
         */
        private String path = "/**";

        /**
         *  白名单
         */
        private String[] excludePath = new String[]{
                "/**/doc.html",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.ico",
                "/**/v3/**"
        };

        /**
         * 是否开启验证,默认为开启
         */
        private boolean enable = true;

        public void setExcludePath(String[] excludePath) {
            this.excludePath = Stream.concat(Stream.of(excludePath), Stream.of(this.excludePath)).toArray(String[]::new);
        }
    }
}
