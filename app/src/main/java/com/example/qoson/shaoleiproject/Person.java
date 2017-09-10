package com.example.qoson.shaoleiproject;

import cn.bmob.v3.BmobObject;

/**
 * Created by qoson on 2017/3/21.
 */

public class Person extends BmobObject {
    private String name;
    private String passWord;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}