package com.xxl.scaffold.domain.file.service;

import cn.hutool.core.io.FileMagicNumber;
import cn.hutool.core.lang.Assert;
import com.xxl.scaffold.toolkit.file.FileUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

import static cn.hutool.core.io.FileMagicNumber.MP4;

/**
 * @author xxl
 * @since 2024/3/13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    @Value("${file.save.path}")
    private String filePath;

    private final HttpServletResponse response;

    public String saveFile(MultipartFile file) {
        return null;
    }

    private void check(String songName, FileMagicNumber...type)  {
        boolean match = Arrays.stream(type).anyMatch(t -> t.getExtension().equalsIgnoreCase(FileUtils.getFileSuffix(songName)));
        Assert.isTrue(match,"不支持的文件格式 = "+ FileUtils.getFileSuffix(songName));
    }

    public void dowload(String id)  {
    }
}


