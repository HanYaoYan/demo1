package com.informationsharingsystem.java_informationsharingsystem.service;

import com.informationsharingsystem.java_informationsharingsystem.entity.User;
import com.informationsharingsystem.java_informationsharingsystem.repository.UserRepository;
import com.informationsharingsystem.java_informationsharingsystem.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 包含用户的 Optional 对象
     */
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    /**
     * 注册新用户
     *
     * @param user 用户对象
     * @return 注册成功的用户
     */
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * 删除用户
     *
     * @param user 要删除的用户对象
     */
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}