package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class searchDataController {
    @PostMapping("/search")
    public String showSearch(){
        return "search";
    }
}
