package com.informationsharingsystem.java_informationsharingsystem.repository;

import com.informationsharingsystem.java_informationsharingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // 根据用户名查询用户
}