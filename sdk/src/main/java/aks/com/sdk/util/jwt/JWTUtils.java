package aks.com.sdk.util.jwt;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import aks.com.sdk.exception.ServiceException;

import java.util.Map;
import java.util.Objects;

/**
 * @author xxl
 * @since 2024/2/28
 */
public class JWTUtils {

    private static final String JWT_HEADER = "e10adc3949ba59abbe56e057f20f883e";
    private static final String EXPIRE_TIME = "expire_time";

    /**
     * 毫秒为单位，保存时间为15天
     */
    public static final long HALF_A_MONTH = 1000 * 60 * 60 * 24 * 15;

    public static String createToken(long time,Map<String, Object> payload) {
        Map<String, Object> map = Objects.requireNonNull(payload);
        map.put(EXPIRE_TIME, System.currentTimeMillis() + time);
        return JWTUtil.createToken(map, JWT_HEADER.getBytes());
    }

    public static String createToken(Map<String, Object> payload) {
        return createToken(HALF_A_MONTH, payload);
    }

    public static String verifyToken(String token) {
        if (JWTUtil.verify(token, JWT_HEADER.getBytes())) {
            JWT jwt = JWTUtil.parseToken(token);
            if (Long.parseLong(jwt.getPayload(EXPIRE_TIME).toString()) < System.currentTimeMillis()) {
                throw new ServiceException("TOKEN过期");
            }
            return token;
        }
        throw new ServiceException("TOKEN异常");
    }
}
