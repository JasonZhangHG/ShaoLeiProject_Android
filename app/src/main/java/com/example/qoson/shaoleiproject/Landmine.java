package com.example.qoson.shaoleiproject;

import android.content.Context;
import android.widget.Button;

public class Landmine extends Button {
	//是否有地雷
	public boolean isLandmine=false;
	//是否为空白页
	public boolean isNull=false;
	//记录长按了第几次
	public  int onlong=0;
	public Landmine(Context context) {
		super(context);
	}
}
