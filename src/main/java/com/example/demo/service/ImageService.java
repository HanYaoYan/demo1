package com.example.demo.service;

import com.example.demo.model.FileInfo;
import com.example.demo.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public FileInfo saveImage(FileInfo fileInfo) {
        return imageRepository.save(fileInfo); // 修正调用
    }
}