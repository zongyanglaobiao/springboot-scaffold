package com.template.core.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.template.exception.CommonException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;


/**
 * 通用文件工具类
 * @author xxl
 * @since 2023/11/23
 */
public class FileUtils {

    /**
     * 上传
     *
     * @param file     文件
     * @param savePath 保存路径
     * @return 字符串
     * @throws CommonException 使用
     */
    public static String upload(InputStream file, String savePath) throws CommonException {
        if (ObjectUtil.isNull(file) || StrUtil.isBlank(savePath)) {
            return  null;
        }
        //路径不存就创建
        File touch = FileUtil.touch(new File(savePath));
        try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(touch))){
            file.transferTo(writer);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("FileUtils：文件写出失败");
        }
        return touch.getPath();
    }

    /**
     * 下载
     *
     * @param filePath 文件路径
     * @return 字节[]
     * @throws CommonException 使用
     */
    public static byte[] download(String filePath) throws CommonException {
        //路径不存就创建
        File touch = FileUtil.touch(new File(filePath));
        if (!touch.exists()) {
            throw new CommonException(String.format("%s不存在",filePath));
        }
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(touch))){
            return inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("FileUtils：文件读入失败");
        }
    }

    /**
     * 网页下载
     *
     * @param filePath 文件路径
     * @param response 响应
     * @param fileName 文件名
     * @throws CommonException 使用
     */
    public static void webDownload(String filePath, HttpServletResponse response,String fileName) throws CommonException {
        try {
            byte[] download = download(filePath);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLUtil.encode(fileName));
            response.addHeader("Content-Length", "" + download.length);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType("application/octet-stream;charset=UTF-8");
            IoUtil.write(response.getOutputStream(), true, download);
        } catch (CommonException | IOException e) {
            e.printStackTrace();
            throw new CommonException("web下载失败");
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
        if (StrUtil.isBlank(path) || path.contains(dot)) {
            return null;
        }
        return path.substring(path.lastIndexOf(dot) + 1);
    }

    /**
     * 获取文件名
     *
     * @param path 路径
     * @return 字符串
     */
    public static String getFileName(String path) {
        if (StrUtil.isBlank(path)) {
            return null;
        }
        return path.substring(path.lastIndexOf("\\") + 1);
    }
}
