package com.animal.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @PostMapping
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        // 创建上传目录
        File uploadDir = new File("uploads/");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        // 生成唯一文件名
        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
            ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
        // 保存文件
        file.transferTo(new File(uploadDir, fileName));
        Map<String, String> result = new HashMap<>();
        result.put("url", "/uploads/" + fileName);
        return result;
    }
}
