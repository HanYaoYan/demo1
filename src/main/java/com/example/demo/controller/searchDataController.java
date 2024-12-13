package com.example.demo.controller;

import com.example.demo.model.FileInfo;
import com.example.demo.service.FileService;
import com.example.demo.util.AuthorizingUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class searchDataController {
    @Autowired
    private FileService fileService;

    @GetMapping("/search")
    public String showSearch(){
        return "search";
    }

    @PostMapping("/search/data")
    public ResponseEntity<Map<String,String>> getSearchData(
            @RequestParam("filename") String name,
            HttpServletRequest request
    ) {
        //重构id
        String username= AuthorizingUtil.getJwtUsername(request);
        String Id=username+":"+name;

        Map<String, String> response = new HashMap<>();
        try{
            FileInfo fileinfo=fileService.findByName(Id);
            response.put("status", "success");
            response.put("name", fileinfo.getName());
            response.put("URL", fileinfo.getURL());
            response.put("size", String.valueOf(fileinfo.getSize()));
            response.put("date", fileinfo.getDate());
            return ResponseEntity.ok(response);
        }catch(FileNotFoundException e){
            // 如果未找到文件信息，构建失败响应
            response.put("status", "error");
            response.put("message", "文件未找到");
            return ResponseEntity.status(404).body(response); // 返回 404 Not Found 和错误消息
        }
    }
}
