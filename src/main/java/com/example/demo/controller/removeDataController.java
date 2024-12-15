package com.example.demo.controller;

import com.example.demo.model.FileInfo;
import com.example.demo.service.FileService;
import com.example.demo.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;//new！！！
import com.example.demo.util.AuthorizingUtil;//new!!!

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
    public ResponseEntity<Map<String,String>> RemoveDataController(
            @RequestParam("oldfile") String name,
            HttpServletRequest request) throws FileNotFoundException {
            //上面的new!!!!
        //重构id
        String username=AuthorizingUtil.getJwtUsername(request);
        String Id=username+":"+name;
        Map<String, String> response = new HashMap<>();
        try {
            FileInfo fileInfo = fileService.findByName(Id);

            /*//new!!!
            //没找到文件，你是不是漏了
            if (fileInfo == null) {
                response.put("message", "未找到文件: " + name);
                return ResponseEntity.status(404).body(response);
            }

            //@begin new!!!
            //验证找到的文件的权限
            if (!AuthorizingUtil.judgeUser(fileInfo.getUser(),request)){
                //返回什么错误信息，你决定决定,我这里随便写写
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message","你没有操作该文件的权限");
                return ResponseEntity.status(500).body(errorResponse);
            }
            //@end new!!!*/

            //检查jwt是否有效
            JWTUtil jwtUtil=new JWTUtil();
            //检查jwt是否有效
            if(!jwtUtil.validateToken(AuthorizingUtil.getJwtFromCookies(request),username)){
                response.put("message","会话已无效，请重新登录");
                return ResponseEntity.status(500).body(response);
            }

            //构建响应体，删除文件
            response.put("status", "success");
            response.put("name", fileInfo.getName());
            response.put("URL", fileInfo.getURL());
            response.put("size", String.valueOf(fileInfo.getSize()));
            response.put("date", fileInfo.getDate());
            fileService.deleteByName(Id);
            return ResponseEntity.status(200).body(response);
        } catch (FileNotFoundException e) {
            System.out.println("文件找不到" + e.getMessage());
            response.put("status","error");
            response.put("message","找不到文件");
            return ResponseEntity.status(404).body(response);
        }

    }

}
