package com.example.qoson.shaoleiproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cn.bmob.v3.Bmob;


public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private long firstBack = -1;
    protected void onCreate(Bundle savedInstanceState) {
         intent = new Intent(MainActivity.this,MusicServer.class);
        super.onCreate(savedInstanceState);
        //第一：默认初始化
        Bmob.initialize(this, "b0e16051113eb6a7baf094c7baa4b5e3");
        setContentView(R.layout.activity_main);
        startService(intent);
        findViewById(R.id.btnMainEasy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EasyActivity.class);
                startActivity(intent);
            }
        });
       findViewById(R.id.btnMainHard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HardActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnMainMiddle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MiddleActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnMainLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        findViewById(R.id.btnMainResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        startService(intent);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        startService(intent);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(MainActivity.this,MusicServer.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstBack < 2000) {
                super.onBackPressed();
            } else {
                firstBack = System.currentTimeMillis();
                Toast.makeText(this, "确定退出游戏？", Toast.LENGTH_SHORT).show();
            }
        }


}

