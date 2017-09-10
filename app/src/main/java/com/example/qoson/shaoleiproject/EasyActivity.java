package com.example.qoson.shaoleiproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qoson.shaoleiproject.GreenDao.DBBeanResultDaoUtils;
import com.example.qoson.shaoleiproject.GreenDao.ResultDao;

import java.util.Random;


public class EasyActivity extends AppCompatActivity {

    private TextView txt_time2,txt_landmine2;
    private ImageButton ibn_games2;
    private TableLayout tlt_landmine2;

    private Handler handler=new Handler();
    private int times=0;//用于计算的时间



    private Landmine landmine[][];//地雷区域
    private int landmineRows=9;//地雷行
    private int landmineColumns=9;//地雷列
    private int landminenumber=6;//地雷数量
    private int landmineWidth=110;//地雷宽度
    private int leinum=0;//地雷个数计数器
    private int  landmindnumber1=6;//判断游戏是否赢得地雷计数器
    //public int onclickx=-1,onclicky=-1,onclickx1=-1,onclicky1=-1;//禁止onclick的xy坐标
    //private boolean isonclick=false;//是否开启检测Onclick事件
    private static String MainTAG="MainActivity";
    private  static String landmineText;

    private  ResultDao resultDao;//数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(EasyActivity.this,MusicServer.class);
        super.onCreate(savedInstanceState);
        resultDao = new ResultDao();
        startService(intent);
        setContentView(R.layout.activity_easy);
        findViewById();
        setListener();
        OnLoad();

    }

    //获取ID
    private void findViewById() {
        txt_time2=(TextView) findViewById(R.id.txt_time2);
        txt_landmine2=(TextView) findViewById(R.id.txt_landmine2);
        ibn_games2=(ImageButton) findViewById(R.id.ibn_games2);
        tlt_landmine2=(TableLayout) findViewById(R.id.tlt_landmine2);
    }

    private void setListener() {
        ibn_games2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ibn_games2.setImageDrawable(getResources().getDrawable(R.drawable.smile));
                endGamesLandmine();//恢复所有的界面
                createMinearea();//在Landmine[][]中随机生成10个地雷以及事件监听
                showMinearea();//创建地雷区域 9*9的方格
            }
        });

    }

    private void OnLoad() {
        showToast("亲，点击笑脸开始游戏哦");
    }

    private void showToast(String string) {
        Toast.makeText(this,string,Toast.LENGTH_LONG).show();
    }

    //实现计时器的功能
    private Runnable runtime=new Runnable() {
        public void run() {
            //获取系统时间
            long systemtime=System.currentTimeMillis();
            resultDao.setCreatTime(systemtime);
            times++;
            //判断times大小 然后进行界面文字更改
            if(times<10){
                txt_time2.setText("00"+times);
            }else if(times<100){
                txt_time2.setText("0"+times);
            }else{
                txt_time2.setText(times);
            }
            //设定精确时间
            handler.postAtTime(this,systemtime);
            //开启时间
            handler.postDelayed(runtime,1000);
        }
    };
    //启动计时器  要先关闭 在开启
    private void startRunTime(){
        if(times==0){
            handler.removeCallbacks(runtime);
            handler.postDelayed(runtime, 1000);
        }
    }
    //关闭计时器
    private void stopRunTime(){
        handler.removeCallbacks(runtime);
    }

    //恢复所有的设置
    private void endGamesLandmine() {
        times=0;
        landmindnumber1=6;
        txt_landmine2.setText("006");
        txt_time2.setText("000");
        ibn_games2.setImageDrawable(this.getResources().getDrawable(R.drawable.smile));
        tlt_landmine2.removeAllViews();//删除所有的行
    }

    //创建6个地雷
    private void createMinearea() {
        //创建Button数组
        landmine=new  Landmine[landmineRows+1][landmineColumns+1];
    }
    //创建9*9 表格
    private void showMinearea() {
        //创建9*9的表格
        for (int i = 0; i < landmineRows; i++) {
            TableRow tbRow=new TableRow(this);
            //设置tab大小
            TableRow.LayoutParams layoutParams=new TableRow.LayoutParams(landmineWidth,140);
            for (int j = 0; j < landmineColumns; j++) {
                landmine[i][j]=new Landmine(this);
                landmine[i][j].setLayoutParams(layoutParams);
                landmine[i][j].setTextSize(24);
                landmine[i][j].isLandmine=false;//设置没有地雷
                landmine[i][j].isNull=false;//设置全部为空白
                landmine[i][j].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));
                landmine[i][j].setTextColor(0xD3D3D3);//设置字体背景颜色


                //设置按钮的点击事件
                final int x=i;
                final int y=j;
                landmine[i][j].setOnClickListener(new View.OnClickListener() {
                    //所有按钮点击事件
                    public void onClick(View v) {
                        startRunTime();//启动计时器
                        //首先判断是否标记为F
                        if(landmine[x][y].getText().equals("L")){//判断文字是否为F 若为F 假装禁止点击事件
                            landmine[x][y].setTextColor(Color.RED);//设置字体为红色
                            landmine[x][y].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));
                        }else if(landmine[x][y].getText().equals("?")){
                            landmine[x][y].setText(landmineText);
                        }else{
                            landmine[x][y].setTextColor(Color.BLACK);
                            landmine[x][y].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                        }
                        //当点击到是否为地雷
                        if(landmine[x][y].isLandmine==true){
                            showLandmine();//显示所有地雷
                            showToast("游戏失败,您点击地雷了");
                        }else{//在判断上面的文字是否为空白
                            if(landmine[x][y].getText().equals("")){
                                //遍历上下左右是否为空白  该方法与计算扫雷的方法相同 同样是从一般到特殊判断
                                checkLandmineType(x,y,1);
                            }
                        }
                        if(txt_landmine2.getText().equals("000")){
                            //最后一步 检测游戏是否结束
                            checkGame();
                        }
                    }
                });

                //长按事件的编写
                landmine[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        //判断times大小 然后进行界面文字更改
                        if(landmine[x][y].onlong==0){//即初始化值 长按第一次为标记地雷
                            landmineText=landmine[x][y].getText().toString();
                            landmindnumber1-=1;
                            landmine[x][y].setText("L");
                            landmine[x][y].setTextColor(Color.RED);
                            //onclickx=x;onclicky=y;
                            landmine[x][y].onlong=1;
                        }else if(landmine[x][y].onlong==1){//长按第二次为标记为问号
                            landmindnumber1+=1;
                            landmine[x][y].setText("?");
                            landmine[x][y].setEnabled(true);//设置为可按
                            landmine[x][y].onlong=2;
                        }else{//长按第三次为空白
                            landmine[x][y].setText(landmineText);
                            landmine[x][y].setTextColor(0xD3D3D3);
                            landmine[x][y].onlong=0;
                        }

                        if(landmindnumber1<6){
                            txt_landmine2.setText("00"+landmindnumber1);
                        }else if(landmindnumber1<100){
                            txt_landmine2.setText("0"+landmindnumber1);
                        }


                        if(txt_landmine2.getText().equals("000")){
                            //最后一步 检测游戏是否结束
                            checkGame();}
                        return true;
                    }
                });

                tbRow.addView(landmine[i][j]);
            }
            tlt_landmine2.addView(tbRow);
        }
        tenlandmine();//随机生成十个雷
        landmineNumber();//计算地雷数量方法
        this.landmine[1][1].setClickable(false);
    }

    //游戏失败 显示所有的地雷以及数字
    private void showLandmine() {
        for (int i = 0; i < landmineRows; i++) {
            for (int j = 0; j <landmineColumns; j++) {
                landmine[i][j].setTextColor(Color.RED);//显示所有
                landmine[i][j].setClickable(false);//设置按钮不可被点击
                landmine[i][j].setEnabled(false);//设置所有不可按
                stopRunTime();//停止计时器
                ibn_games2.setImageDrawable(this.getResources().getDrawable(R.drawable.sad));
            }
        }
        landmindnumber1=6;//恢复地雷数量
    }

    private void landmineNumber() {//计算地雷数量方法
        for (int i = 0; i < landmineRows; i++) {
            for (int j = 0; j <landmineColumns; j++) {
                if(landmine[i][j].isLandmine!=true){//首先该坐标没有雷
				/*	x-1,y-1		x-1,y		x-1,y+1
				 *  x,y-1		x,y			x,y+1
				 *  x+1,y-1		x+1,y		x+1,y+1
				 *
				 * 由上图可知道 最小的值为 x-1,y-1
				 *
				 * 先判断i-1 和 j-1 都小于0  即 只用判断附近三个值  九宫格判断法
				 * 第一步 先判断顶点坐标
				 */
                    checkLandmineType(i,j,0);//计算地雷数量

                }
            }
        }

    }

    //地雷计数器 初始化
    private void leinumber(int i,int j){
        //当leinum为0 时  不显示
        if(leinum!=0){
            landmine[i][j].setText(leinum+"");
        }else{
            landmine[i][j].setText("");
        }
        leinum=0;//初始化
    }
    private void tenlandmine(){//生成地雷
        Random random=new Random();
        for (int i = 0; i <landminenumber; i++) {
            int x=random.nextInt(8);//作为x坐标
            int y=random.nextInt(8);//作为y坐标
            Log.i("Tag",x+","+y);
            //判断是否有地雷
            if(landmine[x][y].isLandmine!=true){
                landmine[x][y].isLandmine=true;
                landmine[x][y].setText("*");
            }else{
                i--;
            }
        }
    }


    //用来判断 是计算地雷数量 还是判断是否为空白
    private void checkLandmineType(int i,int j,int type){
        //(0,0)点 即左上角点
        if(i==0&&j==0){//这个点只有一个是(0,0)  所有只用判断(1,0)(1,1)(0,1)三个点是否有雷即可
            Xia(i, j,type);
            youXia(i, j,type);
            youZhong(i, j,type);
            if(type==0){leinumber(i,j);}//计数器初始化}
        }

//右上角坐标 (0,landmineColumns-1)
        else if(i==0&&j==landmineColumns-1){
            zuoZhong(i, j,type);
            zuoXia(i, j,type);
            Xia(i, j,type);
            if(type==0){leinumber(i,j);}//计数器初始化
        }

//左下角坐标(landmineRows-1,0)
        else if(i==landmineRows-1&&j==0){
            Shang(i, j,type);
            youShang(i, j,type);
            youZhong(i, j,type);
            if(type==0){leinumber(i,j);}//计数器初始化
        }
//右下角坐标 (landmineRows-1,)
        else if(i==landmineRows-1&&j==landmineColumns-1){
            zuoShang(i, j,type);
            Shang(i, j,type);
            zuoZhong(i, j,type);
            if(type==0){leinumber(i,j);}//计数器初始化

        }

//最上面一行只用算5个坐标
        else if(i==0&&(j>0&&j<landmineColumns-1)){
            zuoZhong(i, j,type);
            youZhong(i, j,type);
            zuoXia(i, j,type);
            Xia(i, j,type);
            youXia(i, j,type);
            if(type==0){leinumber(i,j);}//计数器初始化
        }
        //最下面一行只用算5个坐标
        else if(i==landmineRows-1&&(j>0&&j<landmineColumns-1)){
            zuoZhong(i, j,type);
            youZhong(i, j,type);
            zuoShang(i, j,type);
            Shang(i, j,type);
            youShang(i, j,type);
            if(type==0){leinumber(i,j);}//计数器初始化
        }
//最左边一列  只用据算5个坐标
        else if((i>0&&i<landmineRows-1)&&j==0){
            Shang(i, j,type);
            youShang(i, j,type);
            youZhong(i, j,type);
            Xia(i, j,type);
            youXia(i, j,type);
            if(type==0){leinumber(i,j);}//计数器初始化
        }
        //最右边一列  只用据算5个坐标
        else if((i>0&&i<landmineRows-1)&&j==landmineColumns-1){
            Shang(i, j,type);
            zuoShang(i, j,type);
            zuoZhong(i, j,type);
            zuoXia(i, j,type);
            Xia(i, j,type);
            if(type==0){leinumber(i,j);}//计数器初始化
        }
        //其他中央区域 需要判断8个
        else{
            zuoShang(i, j,type);
            zuoZhong(i, j,type);
            zuoXia(i, j,type);
            Shang(i, j,type);
            Xia(i, j,type);
            youShang(i, j,type);
            youZhong(i, j,type);
            youXia(i, j,type);
            if(type==0){leinumber(i,j);}//计数器初始化
        }
    }


    /**
     * 以下为判断8个坐标是否有地雷  我分别命名为 左上, 左中, 左下 ,上 ,下 ,右上, 右下;
     * 只需要传入x,y坐标 和判断的类型 0(判断地雷数目) 1(判断是否为空白)
     * 返回值为Void
     *
     */

//左上坐标
    private void zuoShang(int i,int j,int type){
        switch (type) {
            case 0:
                if(landmine[i-1][j-1].isLandmine==true){
                    leinum++;
                }
                break;
            case 1:
                if(landmine[i-1][j-1].getText().equals("")){
                    if(landmine[i-1][j-1].isNull==false){//先判断是该坐标是否为null 默认的是不为null 即为false
                        landmine[i-1][j-1].isNull=true;//设置当前的坐标的isNull为true 这样有效避免了死循环
                        checkLandmineType(i-1,j-1,1);
                    }
                    landmine[i-1][j-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                }else{
                    if(landmine[i-1][j-1].getText().equals("L")){
                        landmine[i-1][j-1].setTextColor(Color.RED);
                        landmine[i-1][j-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));
                    }else{
                        landmine[i-1][j-1].setTextColor(Color.BLACK);
                        landmine[i-1][j-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                    }}
                break;
        }

    }
    //左中坐标
    private void zuoZhong(int i,int j,int type){
        switch (type) {
            case 0:
                if(landmine[i][j-1].isLandmine==true){
                    leinum++;
                }
                break;

            case 1:
                if(landmine[i][j-1].getText().equals("")){
                    if(landmine[i][j-1].isNull==false){
                        landmine[i][j-1].isNull=true;
                        checkLandmineType(i,j-1,1);
                    }
                    landmine[i][j-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                }else{
                    if(landmine[i][j-1].getText().equals("L")){
                        landmine[i][j-1].setTextColor(Color.RED);
                        landmine[i][j-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));
                    }else{
                        landmine[i][j-1].setTextColor(Color.BLACK);
                        landmine[i][j-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                    }}
                break;
        }

    }
    //左下坐标
    private void zuoXia(int i,int j,int type){
        switch (type) {
            case 0:
                if(landmine[i+1][j-1].isLandmine==true){
                    leinum++;
                }
                break;

            case 1:
                if(landmine[i+1][j-1].getText().equals("")){
                    if(landmine[i+1][j-1].isNull==false){
                        landmine[i+1][j-1].isNull=true;
                        checkLandmineType(i+1,j-1,1);
                    }
                    landmine[i+1][j-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                }else{
                    if(landmine[i+1][j-1].getText().equals("L")){
                        landmine[i+1][j-1].setTextColor(Color.RED);
                        landmine[i+1][j-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));
                    }else{
                        landmine[i+1][j-1].setTextColor(Color.BLACK);
                        landmine[i+1][j-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                    }}
                break;
        }
    }
    //上坐标
    private void Shang(int i,int j,int type){
        switch (type) {
            case 0:
                if(landmine[i-1][j].isLandmine==true){
                    leinum++;
                }
                break;

            case 1:
                if(landmine[i-1][j].getText().equals("")){
                    if(landmine[i-1][j].isNull==false){
                        landmine[i-1][j].isNull=true;
                        checkLandmineType(i-1,j,1);
                    }
                    landmine[i-1][j].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                }else{
                    if(landmine[i-1][j].getText().equals("L")){
                        landmine[i-1][j].setTextColor(Color.RED);
                        landmine[i-1][j].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));
                    }else{
                        landmine[i-1][j].setTextColor(Color.BLACK);
                        landmine[i-1][j].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                    }
                }
                break;
        }

    }
    //下坐标
    private void Xia(int i,int j,int type){
        switch (type) {
            case 0:
                if(landmine[i+1][j].isLandmine==true){
                    leinum++;
                }
                break;

            case 1:
                if(landmine[i+1][j].getText().equals("")){
                    if(landmine[i+1][j].isNull==false){
                        landmine[i+1][j].isNull=true;
                        checkLandmineType(i+1,j,1);
                    }
                    landmine[i+1][j].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                }else{
                    if(landmine[i+1][j].getText().equals("L")){
                        landmine[i+1][j].setTextColor(Color.RED);//设置字体为红色
                        landmine[i+1][j].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));
                    }else{
                        landmine[i+1][j].setTextColor(Color.BLACK);
                        landmine[i+1][j].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                    }
                }
                break;
        }

    }
    //右上坐标
    private void youShang(int i,int j,int type){
        switch (type) {
            case 0:
                if(landmine[i-1][j+1].isLandmine==true){
                    leinum++;
                }
                break;

            case 1:
                if(landmine[i-1][j+1].getText().equals("")){
                    if(landmine[i-1][j+1].isNull==false){
                        landmine[i-1][j+1].isNull=true;
                        checkLandmineType(i-1,j+1,1);
                    }
                    landmine[i-1][j+1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                }else{
                    if(landmine[i-1][j+1].getText().equals("L")){
                        landmine[i-1][j+1].setTextColor(Color.RED);
                        landmine[i-1][j+1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));
                    }else{
                        landmine[i-1][j+1].setTextColor(Color.BLACK);
                        landmine[i-1][j+1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                    }}
                break;
        }

    }
    //右中坐标
    private void youZhong(int i,int j,int type){
        switch (type) {
            case 0:
                if(landmine[i][j+1].isLandmine==true){
                    leinum++;
                }
                break;

            case 1:
                if(landmine[i][j+1].getText().equals("")){
                    if(landmine[i][j+1].isNull==false){
                        landmine[i][j+1].isNull=true;
                        checkLandmineType(i,j+1,1);
                    }
                    landmine[i][j+1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                }else{
                    if(landmine[i][j+1].getText().equals("L")){
                        landmine[i][j+1].setTextColor(Color.RED);
                        landmine[i][j+1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));

                    }else{
                        landmine[i][j+1].setTextColor(Color.BLACK);
                        landmine[i][j+1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                    }}
                break;
        }

    }
    //右下坐标
    private void youXia(int i,int j,int type){
        switch (type) {
            case 0:
                if(landmine[i+1][j+1].isLandmine==true){
                    leinum++;
                }
                break;
            case 1:
                if(landmine[i+1][j+1].getText().equals("")){
                    if(landmine[i+1][j+1].isNull==false){
                        landmine[i+1][j+1].isNull=true;
                        checkLandmineType(i+1,j+1,1);
                    }
                    landmine[i+1][j+1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                }else{
                    if(landmine[i+1][j+1].getText().equals("L")){
                        landmine[i+1][j+1].setTextColor(Color.RED);
                        landmine[i+1][j+1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_blue));
                    }else{
                        landmine[i+1][j+1].setTextColor(Color.BLACK);
                        landmine[i+1][j+1].setBackgroundDrawable(getResources().getDrawable(R.drawable.square_grey));
                    }}
                break;
        }
    }

    //最后一步 检查游戏是否结束
    boolean game=true;
    private void checkGame(){
        //本游记判断是否赢 就是遍历一遍 看坐标上还有没有雷
        for (int i = 0; i < landmineRows; i++) {
            for (int j = 0; j < landmineColumns; j++) {
                if(landmine[i][j].getText().equals("*")){
                    game=false;
                    //landmindnumber1--;
                    break;
                }else{
                    continue;
                }
            }
        }
        if(game){//即没有一个地雷 则游戏成功
            showLandmine();//显示所有地雷
            resultDao.setUseTime(times);
            DBBeanResultDaoUtils.getInstance().insertOneData(resultDao);
            showToast("恭喜你,游戏胜利了");
        }

    }

}
