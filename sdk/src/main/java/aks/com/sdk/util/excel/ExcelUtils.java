package aks.com.sdk.util.excel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @since 2024/11/28
 */
public class ExcelUtils {


    /**
     * 通过 Hutool 导出数据到 Excel 并写入到 HTTP 响应流
     *
     * @param data 数据，List<Map<String, Object>> 类型
     * @param headers 表头，String 数组，同时也是表头排序顺序
     * @param response HTTP 响应对象
     * @param fileName 下载文件名
     * @throws IOException
     */
    public static void exportExcel(List<Map<String, Object>> data, List<String> headers, HttpServletResponse response, String fileName) throws IOException {
        try (ExcelWriter writer =  ExcelUtil.getWriter(true); ByteArrayOutputStream out = new ByteArrayOutputStream()){
            //设置表头
            headers.forEach(header -> writer.addHeaderAlias(header, header));

            //创建excel
            writer.write(data, true);
            writer.flush(out, true);

            // 设置响应头，指定文件类型和下载方式
            response.setHeader("Content-Disposition", "attachment;filename=" + URLUtil.encode(fileName));
            response.addHeader("Content-Length", "" + out.toByteArray().length);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType("application/octet-stream;charset=UTF-8");

            // 将字节流写入 HTTP 响应输出流
            IoUtil.write(response.getOutputStream(), true, out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}