package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.FileInfo;
//interface：抽象类
public interface ImageRepository extends JpaRepository<FileInfo,String>{

}
