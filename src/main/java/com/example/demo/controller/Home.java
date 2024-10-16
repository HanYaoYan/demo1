package com.example.demo.controller;
//这是一个Home页面类,用于返回登陆界面
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
 public class Home {
    @GetMapping("/")
    public String returnHome(Model model){
        return "register";
    }
}
