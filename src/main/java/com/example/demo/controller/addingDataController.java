package com.example.demo.controller;

import com.example.demo.Repository.FileRepository;
import com.example.demo.service.FileService;
import com.example.demo.service.UserService;
import com.example.demo.util.JWTUtil;
import com.example.demo.util.AuthorizingUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.model.FileInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class addingDataController {  // 确保类名的首字母大写
    @Autowired
    private UserService userService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileService fileService;

    @GetMapping("/add")
    public String showAddPage() {
        return "add";
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addImage
            (@RequestParam("File") MultipartFile file,
             @RequestParam("dataName") String Id,
             @RequestParam("hiddenSubmitTime") String Date,
             HttpServletRequest request
            ) {

        System.out.println("接收到的文件名: " + file.getOriginalFilename());
        System.out.println("接收到的文件大小: " + file.getSize());

        // 确保路径存在
        String directoryPath = "D:\\java\\demo1\\upload_image"; // 使用绝对路径
        File directory = new File(directoryPath);

        // 确保目录存在
        if (!directory.exists()) {
            boolean created = directory.mkdirs(); // 创建目录
            System.out.println("目录创建成功: " + created);
        }

        String sanitizedFileName = file.getOriginalFilename();
        String URL = directoryPath + "\\" + sanitizedFileName; // 使用反斜杠

        try {
            // 保存文件
            System.out.println("准备保存文件: " + URL);
            file.transferTo(new File(URL));

            // 创建 ImageInfo 对象并设置元数据
            FileInfo fileInfo = new FileInfo();

            //重新设计id
            String username=AuthorizingUtil.getJwtUsername(request);
            Id=username+":"+Id;
            fileInfo.setId(Id);
            fileInfo.setName(sanitizedFileName);
            fileInfo.setSize(file.getSize());
            fileInfo.setURL(URL);
            fileInfo.setDate(Date);
            JWTUtil jwtUtil=new JWTUtil();
            fileInfo.setUser(username);

            // 将文件信息保存到数据库
            fileService.saveFile(fileInfo);

            // 返回成功消息作为JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "图片已上传至: " + fileInfo.getURL());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            // 返回错误消息作为JSON
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }

    }

}