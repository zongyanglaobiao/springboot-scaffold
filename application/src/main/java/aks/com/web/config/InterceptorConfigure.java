package aks.com.web.config;

import aks.com.web.config.properties.SystemConfigProperties;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author xxl
 * @since 2023/9/16
 */
@Slf4j
@NonNullApi
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SystemConfigProperties.class)
public class InterceptorConfigure implements WebMvcConfigurer {

    /**
     * 系统中所有的自定义配置应当写在Properties中
     */
    private final SystemConfigProperties properties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        SystemConfigProperties.Authorization authorization = properties.getAuthorization();
        if (authorization.isEnable()) {
            //check login 也可以使用 SaServletFilter
            InterceptorRegistration authInterceptorRegistration = registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()));
            authInterceptorRegistration.addPathPatterns(authorization.getPath());
            authInterceptorRegistration.excludePathPatterns(authorization.getExcludePath());
        }
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了,这里设置2个小时
        config.setMaxAge(360000L);
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
