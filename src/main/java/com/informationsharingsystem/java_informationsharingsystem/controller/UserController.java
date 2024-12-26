package com.informationsharingsystem.java_informationsharingsystem.controller;

import com.informationsharingsystem.java_informationsharingsystem.entity.User;
import com.informationsharingsystem.java_informationsharingsystem.service.UserService;
import com.informationsharingsystem.java_informationsharingsystem.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * 获取当前用户信息
     *
     * @param token JWT 令牌
     * @return 当前用户的信息
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        // 从令牌中提取用户名
        String username = jwtUtils.extractUsername(token.substring(7));
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 更新当前用户信息
     *
     * @param token  JWT 令牌
     * @param updatedUser 更新后的用户信息
     * @return 更新成功消息
     */
    @PutMapping("/me")
    public ResponseEntity<String> updateCurrentUser(
            @RequestHeader("Authorization") String token,
            @RequestBody User updatedUser) {
        // 从令牌中提取用户名
        String username = jwtUtils.extractUsername(token.substring(7));
        Optional<User> existingUserOptional = userService.findByUsername(username);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // 更新用户信息
            existingUser.setUsername(updatedUser.getUsername() != null ? updatedUser.getUsername() : existingUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword() != null ? updatedUser.getPassword() : existingUser.getPassword());
            userService.registerUser(existingUser); // 使用保存方法（会覆盖）

            return ResponseEntity.ok("User updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 删除当前用户
     *
     * @param token JWT 令牌
     * @return 删除成功消息
     */
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteCurrentUser(@RequestHeader("Authorization") String token) {
        // 从令牌中提取用户名
        String username = jwtUtils.extractUsername(token.substring(7));
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            userService.deleteUser(user.get());
            return ResponseEntity.ok("User deleted successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}