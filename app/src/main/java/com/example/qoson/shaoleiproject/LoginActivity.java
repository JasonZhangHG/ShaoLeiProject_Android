package com.example.qoson.shaoleiproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity  extends AppCompatActivity implements View.OnClickListener {

    private EditText etusername;
    private EditText etpassword;
    private Button login;
    private Button sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //这里的AppLication ID 写上自己创建项目得到的那个AppLication ID
        Bmob.initialize(this, "b0e16051113eb6a7baf094c7baa4b5e3");
        initView();
        initialize();
    }

    private void initView() {
//        if (MyApplication.UserID != null){
//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//            this.finish();
//         }
    }

    private void initialize() {
        etusername = (EditText) findViewById(R.id.et_username);
        etpassword = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        sign = (Button) findViewById(R.id.sign);
        sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                final String username = etusername.getText().toString();
                String password = etpassword.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
                    Person person = new Person();
                    person.setName(username);
                    person.setPassWord(password);
                //查找Person表里面id为MyApplication.UserID的数据

                    SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                    String userID = pref.getString("userID","");//第二个参数为默认值


                    if (TextUtils.isEmpty(userID)){
                        Toast.makeText(LoginActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
                    }else {
                        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();

                        bmobQuery.getObject(userID, new QueryListener<Person>() {
                            @Override
                            public void done(Person person, BmobException e) {
                                if(e==null){
                                   Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }else{
                                    Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}