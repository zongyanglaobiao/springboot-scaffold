package aks.com.sdk.util.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jamesaks
 * @since 2025/4/7
 */
public class FileTypeUtils {

    /**
     * 获取文件头
     *
     * @param inputStream 输入流
     * @return 16 进制的文件投信息
     * @throws IOException io异常
     */
    private static String getFileHeader(InputStream inputStream) throws IOException {
        byte[] b = new byte[28];
        inputStream.read(b, 0, 28);
        inputStream.close();
        return bytes2hex(b);
    }

    /**
     * 将字节数组转换成16进制字符串
     *
     * @param src 文件字节数组
     * @return 16进制字符串
     */
    private static String bytes2hex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 判断指定输入流是否是指定文件格式
     *
     * @param inputStream  输入流
     * @param fileTypeEnum 文件格式枚举
     * @return true 是； false 否
     * @throws IOException io异常
     */
    public static boolean isFileType(InputStream inputStream, FileTypeEnum fileTypeEnum) throws IOException {
        if (null == inputStream) {
            return false;
        }
        String fileHeader = getFileHeader(inputStream);
        return fileHeader.toUpperCase().startsWith(fileTypeEnum.getValue());
    }



    /**
     * 文件类型魔数枚举
     * 使用场景：用于判断文件类型
     * 使用方法：FileUtils.isFileType(new FileInputStream(file), FileTypeEnum.XLSX)
     */
    @AllArgsConstructor
    @Getter
    public enum FileTypeEnum {
        /**
         * JPEG
         */
        JPEG("JPEG", "FFD8FF"),

        /**
         * PNG
         */
        PNG("PNG", "89504E47"),

        /**
         * GIF
         */
        GIF("GIF", "47494638"),

        /**
         * TIFF
         */
        TIFF("TIFF", "49492A00"),

        /**
         * Windows bitmap
         */
        BMP("BMP", "424D"),

        /**
         * CAD
         */
        DWG("DWG", "41433130"),

        /**
         * Adobe photoshop
         */
        PSD("PSD", "38425053"),

        /**
         * Rich Text Format
         */
        RTF("RTF", "7B5C727466"),

        /**
         * XML
         */
        XML("XML", "3C3F786D6C"),

        /**
         * HTML
         */
        HTML("HTML", "68746D6C3E"),

        /**
         * Outlook Express
         */
        DBX("DBX", "CFAD12FEC5FD746F "),

        /**
         * Outlook
         */
        PST("PST", "2142444E"),

        /**
         * doc;xls;dot;ppt;xla;ppa;pps;pot;msi;sdw;db
         */
        OLE2("OLE2", "0xD0CF11E0A1B11AE1"),

        /**
         * Microsoft Word/Excel
         */
        XLS_DOC("XLS_DOC", "D0CF11E0"),

        /**
         * Microsoft Access
         */
        MDB("MDB", "5374616E64617264204A"),

        /**
         * Word Perfect
         */
        WPB("WPB", "FF575043"),

        /**
         * Postscript
         */
        EPS_PS("EPS_PS", "252150532D41646F6265"),

        /**
         * Adobe Acrobat
         */
        PDF("PDF", "255044462D312E"),

        /**
         * Windows Password
         */
        PWL("PWL", "E3828596"),

        /**
         * ZIP Archive
         */
        ZIP("ZIP", "504B0304"),

        /**
         * ARAR Archive
         */
        RAR("RAR", "52617221"),

        /**
         * WAVE
         */
        WAV("WAV", "57415645"),

        /**
         * AVI
         */
        AVI("AVI", "41564920"),

        /**
         * Real Audio
         */
        RAM("RAM", "2E7261FD"),

        /**
         * Real Media
         */
        RM("RM", "2E524D46"),

        /**
         * Quicktime
         */
        MOV("MOV", "6D6F6F76"),

        /**
         * Windows Media
         */
        ASF("ASF", "3026B2758E66CF11"),

        /**
         * MIDI
         */
        MID("MID", "4D546864"),
        /**
         * xlsx
         */
        XLSX("XLSX", "504B0304"),
        /**
         * xls
         */
        XLS("XLS", "D0CF11E0A1B11AE1");

        private final String key;
        private final String value;
    }
}

