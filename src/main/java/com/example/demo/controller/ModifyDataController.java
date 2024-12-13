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
import com.example.demo.util.AuthorizingUtil;//new!!!
import jakarta.servlet.http.HttpServletRequest;//new!!!

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
            @RequestParam("oldname") String oldname,
            HttpServletRequest request) {

        Date now = new Date();
        Map<String, String> response = new HashMap<>();

        //重新设计新旧id，id的样式为“用户姓名：文件名字”
        String username=AuthorizingUtil.getJwtUsername(request);
        String oldId=username+":"+oldname;
        String newId = username +":"+newname;

        try {
            //查找旧文件
            FileInfo oldFile = fileService.findByName(oldId);
           /* if (oldFile == null) {
                response.put("message", "未找到文件或你没有操作该文件的权力: ");
                return ResponseEntity.status(404).body(response);
            }

            //@begin new!!!
            //验证找到的文件的权限
            if (!AuthorizingUtil.judgeUser(oldFile.getUser(),request)){
                //返回什么错误信息，你决定决定,我这里随便写写
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message","你没有操作该文件的权力");
                return ResponseEntity.status(404).body(errorResponse);
            }
            //@end new!!!*/

            //更新文件信息
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(now);
            oldFile.setDate(formattedDate);

            if (newFile != null && !newFile.isEmpty()) {
                oldFile.setSize(newFile.getSize());
                oldFile.setName(newFile.getOriginalFilename());
                oldFile.setId(newId);
            }

            if (newname != null && !newname.isEmpty()) {
                oldFile.setId(newId);
                oldFile.setName(newname);
            }

            // 更新文件信息
            fileService.deleteByName(oldId); // 删除原先的文件记录
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