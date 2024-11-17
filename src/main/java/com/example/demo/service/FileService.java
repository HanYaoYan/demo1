package com.example.demo.service;

import com.example.demo.model.FileInfo;
import com.example.demo.Repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public FileInfo saveFile(FileInfo fileInfo) {
        return fileRepository.save(fileInfo); // 修正调用
    }
    public FileInfo fineByName(String fileName){
        return fileRepository.getReferenceById(fileName);
    }
}