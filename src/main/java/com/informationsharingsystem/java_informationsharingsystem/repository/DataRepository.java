package com.informationsharingsystem.java_informationsharingsystem.repository;

import com.informationsharingsystem.java_informationsharingsystem.entity.Data;
import com.informationsharingsystem.java_informationsharingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataRepository extends JpaRepository<Data, Long> {
    List<Data> findByUser(User user); // 根据用户查询数据
}