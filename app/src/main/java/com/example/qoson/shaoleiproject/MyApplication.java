package com.example.qoson.shaoleiproject;

import android.app.Application;

import com.example.qoson.shaoleiproject.GreenDao.DBBeanResultDaoUtils;

/**
 * Created by qoson on 2017/3/21.
 */

public class MyApplication  extends Application {

    public static String UserID;

    @Override
    public void onCreate() {
        super.onCreate();
        DBBeanResultDaoUtils.Init(getApplicationContext());
    }
}
