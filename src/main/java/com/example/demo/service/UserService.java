package com.example.demo.service;
/**
 * 这是一个用户的服务端类,用于对数据库进行一些复杂的操作
 */

import com.example.demo.Repository.UserRepository;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User saveUser(User user){

        return userRepository.save(user);
    }
    public boolean verifyLogin(String username,String password){
        //或许我只要在这里截住就可以获取到正确的账号密码了?
        User user=userRepository.findByUsername(username);
        if(user==null){
            return false;
        }
        return Objects.equals(user.getPassword(), password);
    }
}
