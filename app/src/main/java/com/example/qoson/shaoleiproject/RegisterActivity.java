package com.example.qoson.shaoleiproject;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends AppCompatActivity  {

    private EditText etusername;
    private EditText etpassword;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    private void initialize() {
        etusername = (EditText) findViewById(R.id.et_username2);
        etpassword = (EditText) findViewById(R.id.et_password2);
        register = (Button) findViewById(R.id.sign2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerData();
            }
        });

    }


    /**
     * 注册
     */
    private void registerData() {
        final Person person = new Person();
        final String name = etusername.getText().toString();
        final String password = etpassword.getText().toString();
        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            person.setName(name);
            person.setPassWord(password);
            person.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e==null){
                       ////用SharedPreferences 存储UserID
                        SharedPreferences pref = RegisterActivity.this.getSharedPreferences("data",MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("userID",s);
                        editor.commit();
                        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        RegisterActivity.this.finish();
                    }else {
                        Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}