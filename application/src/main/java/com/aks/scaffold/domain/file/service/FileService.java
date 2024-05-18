package com.aks.scaffold.domain.file.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public void download(String id)  {
    }
}


