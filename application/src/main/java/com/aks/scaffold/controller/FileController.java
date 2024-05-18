package com.aks.scaffold.controller;

import com.aks.scaffold.domain.file.service.FileService;
import com.aks.sdk.resp.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 *
 * @author xxl
 * @since 2024/3/13
 */
@RestController
@RequestMapping("/file")
@Validated
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("upload")
    public RespEntity<String> upload(@RequestBody MultipartFile file) {
        return RespEntity.success(fileService.saveFile(file));
    }

    @GetMapping("download/{fileId}")
    public  void download(@PathVariable("fileId") String fileId) {
        fileService.download(fileId);
    }
}
