package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.FileInfo;
//interface：抽象类
public interface FileRepository extends JpaRepository<FileInfo,String>{

}
