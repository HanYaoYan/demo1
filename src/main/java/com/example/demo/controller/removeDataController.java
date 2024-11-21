package com.example.demo.controller;

import com.example.demo.Repository.FileRepository;
import com.example.demo.model.FileInfo;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
@Controller
public class removeDataController {

    @Autowired
    FileService fileService;
    @GetMapping("/remove")
    public String showRemove(){
        return "remove";
    }

    @PostMapping("/remove")
    public ResponseEntity<Map<String,String>> RemoveDataController(@RequestParam("oldfile") String name) throws FileNotFoundException {
        Map<String, String> response = new HashMap<>();
        try {
            FileInfo fileInfo = fileService.findByName(name);
            response.put("status", "success");
            response.put("name", fileInfo.getName());
            response.put("URL", fileInfo.getURL());
            response.put("size", String.valueOf(fileInfo.getSize()));
            response.put("date", fileInfo.getDate());
            fileService.deleteByName(name);
            return ResponseEntity.status(200).body(response);
        } catch (FileNotFoundException e) {
            System.out.println("文件找不到" + e.getMessage());
            response.put("statue","error");
            response.put("message","找不到文件");
            return ResponseEntity.status(404).body(response);
        }

    }

}
