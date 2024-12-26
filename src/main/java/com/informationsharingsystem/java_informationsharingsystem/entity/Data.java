package com.informationsharingsystem.java_informationsharingsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "data") // 对应数据库的表名
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Long id;

    @ManyToOne // 数据与用户的多对一关系
    @JoinColumn(name = "user_id", nullable = false) // 外键关联到 "users" 表
    private User user;

    @Column(columnDefinition = "json", nullable = false) // 数据以 JSON 格式存储
    private String content;

    // 无参构造函数 (JPA 要求)
    public Data() {}

    // 有参构造函数
    public Data(User user, String content) {
        this.user = user;
        this.content = content;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", user=" + user +
                ", content='" + content + '\'' +
                '}';
    }
}