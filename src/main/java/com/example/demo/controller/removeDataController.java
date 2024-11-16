package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class removeDataController {

    @PostMapping("/remove")
    public String showRemove(){
        return "remove";
    }

    /*@GetMapping("/remove")
    public ResponseEntity<Map<String, String>> removeImage(){

    }*/
}
