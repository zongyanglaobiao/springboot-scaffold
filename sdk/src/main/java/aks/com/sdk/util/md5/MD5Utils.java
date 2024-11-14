package aks.com.sdk.util.md5;


import cn.hutool.crypto.digest.DigestUtil;

/**
 * @author xxl
 * @since 2024/2/28
 */
public class MD5Utils {

    public static String encrypt(String password) {
        return DigestUtil.md5Hex(password);
    }

    public static boolean decrypt(String password,String ciphertext) {
        return encrypt(password).equals(ciphertext);
    }

}
