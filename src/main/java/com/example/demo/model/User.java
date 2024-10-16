package com.example.demo.model;
/**
 * 这是一个user类,用于记录用户输入的属性,后续将用该类将属性导入数据库
 */

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="login")
public class User {
    @Id
    private String username;

    private String password;


    public User(){

    }

    public User(String username, String password){
        this.password=password;
        this.username=username;
    }


    public String getusername() {
        return username;
    }
    public void setusername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
