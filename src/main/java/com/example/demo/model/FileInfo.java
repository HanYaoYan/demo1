package com.example.demo.model;

import com.example.demo.util.AuthorizingUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.servlet.http.HttpServletRequest;

import static com.example.demo.util.AuthorizingUtil.getJwtUsername;

@Entity
@Table(name="Mulfile")
public class FileInfo {
    @Id
    private String Id;

    private String name;
    private String URL;
    private long size;
    private String Date;


    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    private String User;
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public FileInfo(String name, String URL, long size) {
        this.name = name;
        this.URL=URL;
        this.size=size;
    }

    public FileInfo() {
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
