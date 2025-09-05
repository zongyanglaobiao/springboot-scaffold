package aks.com.web.toolkit.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


/**
 * 通用文件工具类
 * @author xxl
 * @since 2023/11/23
 */
@Slf4j
public final class FileUtils {

    /**
     * 保存文件
     *
     * @param file     文件
     * @param savePath 保存路径
     * @return 路径
     * @throws RuntimeException 使用
     */
    public static String saveFile(InputStream file, String savePath) {
        if (ObjectUtil.isNull(file) || StrUtil.isBlank(savePath)) {
            return null;
        }

        File touch;
        try {
            //路径不存就创建
            touch = FileUtil.touch(new File(savePath));
        } catch (Exception e) {
            throw new RuntimeException("文件路径创建失败: " + e.getMessage());
        }

        try(BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(touch))) {
            file.transferTo(writer);
            file.close();
        } catch (Exception e) {
            throw new RuntimeException("文件文件保存失败: " + e.getMessage());
        }
        return touch.getPath();
    }

    /**
     * 读取文件
     *
     * @param filePath 文件路径带文件名字
     * @return 字节[]
     * @throws RuntimeException 使用
     */
    public static byte[] readFile(String filePath){
        //路径不存就创建
        File touch = FileUtil.touch(new File(filePath));
        if (!touch.exists()) {
            throw new RuntimeException(String.format("%s不存在",filePath));
        }
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(touch))){
            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("FileUtils文件读取失败: " + e.getMessage());
        }
    }

    /**
     * 网络下载
     *
     * @param dataByte 数据
     * @param response 响应
     * @param fileName 文件名
     * @throws RuntimeException 使用
     */
    public static void webDownload(byte[] dataByte, HttpServletResponse response,String fileName) throws RuntimeException {
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLUtil.encode(fileName));
            response.addHeader("Content-Length", "" + dataByte.length);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType("application/octet-stream;charset=UTF-8");
            IoUtil.write(response.getOutputStream(), true, dataByte);
        } catch (Exception  e) {
            throw new RuntimeException("WEB下载文件失败: " + e.getMessage());
        }
    }


    /**
     * 获取文件后缀
     *
     * @param path 路径
     * @return 字符串
     */
    public static String getFileSuffix(String path){
        final String dot = ".";
        return !StrUtil.isBlank(path) && path.contains(dot) ? path.substring(path.lastIndexOf(dot) + 1) : null;
    }

    /**
     * 获取文件名
     *
     * @param path 路径
     * @return 字符串
     */
    public static String getFileName(String path) {
        if (Objects.isNull(path)) {
            return null;
        }

        //解决linux系统和windows系统存储路径不一样
        int indexOf ;
        if ((indexOf = path.lastIndexOf("\\")) != -1) {
            return path.substring(indexOf + 1);
        }else if ((indexOf = path.lastIndexOf("/")) != -1) {
            return path.substring(indexOf + 1);
        }
        return null;
    }

    /**
     *  获取完整的URL
     * @param request 请求
     * @param requestPath 请求path
     * @return path
     */
    public static String getUrl(HttpServletRequest request,String requestPath) {
        // 获取协议（如http或https）
        String scheme = request.getScheme();
        // 获取服务器名称（如localhost或具体域名）
        String serverName = request.getServerName();
        // 获取服务器端口号
        int serverPort = request.getServerPort();
        // 获取请求URI（如/endpoint）
        String requestUri = request.getRequestURI();
        // 组合成完整的请求URL
        return scheme + "://" + serverName + ":" + serverPort + requestPath;
    }

    /**
     * 创建文件路徑
     * @param path 保存路径
     * @param dirName 目录名称
     * @param fileName 文件名稱, 例如：20250623180510_Test.xlsx其中Test.xlsx
     * @return 返回绝对路径
     */
    public static String generateTimestampedFilePath(String path,String dirName,String fileName) {
        if (StrUtil.isBlank(path) || StrUtil.isBlank(fileName)){
            return null;
        }

        try {
            // 获取当前日期目录和时间戳
            String dateDir = DateUtil.format(DateUtil.date(), "yyyy-MM-dd");
            String timestamp = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
            // 拼接最终文件名
            fileName = timestamp + "_" + fileName;
            // 使用 Path 安全拼接路径（自动适配 Windows/Mac/Linux）共享文件夹个可x
            Path futhPath;
            if (StrUtil.isNotBlank(dirName)) {
                futhPath = Paths.get(path, dirName, dateDir, fileName);
            } else {
                futhPath = Paths.get(path, dateDir, fileName);
            }
            // 自动创建目录（到父目录为止）
            FileUtil.mkdir(futhPath.toFile().getParentFile());
            // 返回最终绝对路径
            String absolutePath = futhPath.toAbsolutePath().toString();
            log.info("generate file path => {}", absolutePath);
            return absolutePath;
        } catch (Exception e) {
            log.error("generate file path error => ", e);
            return null;
        }
    }

    public static String generateTimestampedFilePath(String path,String fileName) {
        return generateTimestampedFilePath(path, null, fileName);
    }
}
