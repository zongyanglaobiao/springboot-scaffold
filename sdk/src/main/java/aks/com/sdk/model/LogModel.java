package aks.com.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class LogModel {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static  String FORMAT_ZH = """
            logModel{
                请求URL: %s
                请求IP: %s
                请求方法: %s
                执行方法: %s
                请求参数: %s
                响应结果: %s
                请求异常: %s
                请求时间: %s
                响应时间: %s
                请求耗时：: %s 毫秒
            }
            """;

    private final static  String FORMAT_EN = """
            logModel{
                url: %s
                ip: %s
                method: %s
                execution method: %s
                param: %s
                result: %s
                exception: %s
                request time: %s
                response time: %s
                total time: %s ms
            }
            """;

    private String  requestUrl;

    private String  aopPoint;

    private String  requestParams;

    private String  responseResult;

    private String exceptionInfo;

    private LocalDateTime requestTime;

    private LocalDateTime responseTime;

    private String requestIp;

    private String requestMethod;

    public void log(boolean isZh) {
        String fmt = String.format(isZh ? FORMAT_ZH : FORMAT_EN,
                requestUrl,
                requestIp,
                requestMethod,
                aopPoint,
                requestParams,
                responseResult,
                exceptionInfo,
                requestTime.format(FORMATTER),
                responseTime.format(FORMATTER),
                (Duration.between(requestTime, responseTime).toMillis()));
        log.info(fmt);
    }

    public void log() {
        log(true);
    }
}
