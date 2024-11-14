package aks.com.sdk.model;

import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xxl
 * @since 2024/9/14
 */
@Slf4j
@AllArgsConstructor
@Accessors(chain = true)
public class LogModel {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String  requestUrl;

    private String  aopPoint;

    private String  requestParams;

    private String  responseResult;

    private String exceptionInfo;

    private LocalDateTime requestTime;

    private LocalDateTime  responseTime;

    private String  requestIp;

    public void log() {
        log.info(this.toString());
    }

    @Override
    public String toString() {
        final String fmt = """
                    logModel{
                        请求URL: %s
                        请求IP: %s
                        执行方法: %s
                        请求参数: %s
                        响应结果: %s
                        请求异常: %s
                        请求时间: %s
                        响应时间: %s
                        请求耗时：: %s 毫秒
                    }
                    """;
        return String.format(fmt,
                requestUrl,
                requestIp,
                aopPoint,
                requestParams,
                responseResult,
                exceptionInfo,
                requestTime.format(FORMATTER),
                responseTime.format(FORMATTER),
                (Duration.between(requestTime, responseTime).toMillis()));
    }
}
