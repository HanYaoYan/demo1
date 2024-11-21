package com.example.demo.controller;

import com.example.demo.model.FileInfo;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@Controller
public class ModifyDataController {

    @Autowired
    private FileService fileService;

    @GetMapping("/modify")
    public String showModify() {
        return "modify";
    }

    @PostMapping("/modifyFile")
    public ResponseEntity<Map<String, String>> modifyFileController(
            @RequestParam(value = "newFile", required = false) MultipartFile newFile,
            @RequestParam(value = "newname", required = false) String newname,
            @RequestParam("oldname") String oldname) {

        Date now = new Date();
        Map<String, String> response = new HashMap<>();

        try {
            FileInfo oldFile = fileService.findByName(oldname);
            if (oldFile == null) {
                response.put("message", "未找到文件: " + oldname);
                return ResponseEntity.status(404).body(response);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(now);
            oldFile.setDate(formattedDate);

            if (newFile != null && !newFile.isEmpty()) {
                oldFile.setSize(newFile.getSize());
                oldFile.setName(newFile.getOriginalFilename());

            }

            if (newname != null && !newname.isEmpty()) {
                oldFile.setId(newname);
            }

            // 更新文件信息
            fileService.deleteByName(oldname); // 删除原先的文件记录
            fileService.saveFile(oldFile); // 保存更新后的文件信息

            response.put("message", "文件已更新至: " + oldFile.getURL());
            response.put("status", "success");
            response.put("name", oldFile.getName());
            response.put("URL", oldFile.getURL());
            response.put("size", String.valueOf(oldFile.getSize()));
            response.put("date", oldFile.getDate());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "文件修改失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}