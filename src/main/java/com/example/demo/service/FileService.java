package com.example.demo.service;

import com.example.demo.model.FileInfo;
import com.example.demo.Repository.FileRepository;
import com.example.demo.util.AuthorizingUtil;
import com.example.demo.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public FileInfo saveFile(FileInfo fileInfo) throws FileNotFoundException {

        return fileRepository.save(fileInfo); // 修正调用
    }
    public FileInfo findByName(String fileName) throws FileNotFoundException {
        return fileRepository.findById(fileName).orElseThrow(() ->
                new FileNotFoundException("文件未找到: " + fileName)
        );
    }
    public void deleteByName(String fileName) {
                fileRepository.deleteById(fileName);
    }
}