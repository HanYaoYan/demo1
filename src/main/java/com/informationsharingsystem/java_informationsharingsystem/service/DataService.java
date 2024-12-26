package com.informationsharingsystem.java_informationsharingsystem.service;

import com.informationsharingsystem.java_informationsharingsystem.entity.Data;
import com.informationsharingsystem.java_informationsharingsystem.entity.User;
import com.informationsharingsystem.java_informationsharingsystem.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataService {

    private final DataRepository dataRepository;

    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    // 保存数据
    public Data saveData(Data data) {
        return dataRepository.save(data);
    }

    // 根据 ID 查找数据
    public Optional<Data> findDataById(Long id) {
        return dataRepository.findById(id);
    }

    // 查询用户的所有数据
    public List<Data> findDataByUser(User user) {
        return dataRepository.findByUser(user);
    }

    // 删除数据
    public void deleteData(Data data) {
        dataRepository.delete(data);
    }
}