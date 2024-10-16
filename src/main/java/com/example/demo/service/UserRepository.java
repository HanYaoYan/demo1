package com.example.demo.service;
/**
 * 采用Hibernate框架,使用对象关系映射来管理数据库
 * 该类负责直接与数据库进行交户,进行一些简单的插入读取与删除操作
 * 这个地方超级nb!!你只需要按照命名规定命名方法就行
 * Spring Data api 会根据你的名字自动帮你生成代码以实现功能
 */

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,String> {
    User findByUsername(String username);

}
