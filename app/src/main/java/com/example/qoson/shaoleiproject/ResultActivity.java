package com.example.qoson.shaoleiproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qoson.shaoleiproject.GreenDao.DBBeanResultDaoUtils;
import com.example.qoson.shaoleiproject.GreenDao.ResultDao;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private ListView lvResultActivity;
    private List<ResultDao> resultDaoList = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        lvResultActivity = (ListView) findViewById(R.id.lvResultActivity);
        resultDaoList = DBBeanResultDaoUtils.getInstance().queryAllData();
        if (resultDaoList != null &&resultDaoList.size()>0){
            for (int i = 0; i < resultDaoList.size(); i++) {
                for (int j = i; j < resultDaoList.size(); j++) {
                    if (resultDaoList.get(i).getUseTime() >resultDaoList.get(j).getUseTime()) {
                        int time = resultDaoList.get(i).getUseTime();
                        resultDaoList.get(i).setUseTime(resultDaoList.get(j).getUseTime());
                        resultDaoList.get(j).setUseTime(time);
                    }

                }
            }
            myAdapter = new MyAdapter(resultDaoList);
            lvResultActivity.setAdapter(myAdapter);
        }
    }

  public class MyAdapter extends BaseAdapter{

      private List<ResultDao> resultDaoList;
      private LayoutInflater  inflater;
      private MyVidewHolder myViewHolder;

      public MyAdapter(List<ResultDao> resultDaoList) {
          this.resultDaoList = resultDaoList;
          inflater = LayoutInflater.from(ResultActivity.this);
      }


      @Override
      public int getCount() {
          return resultDaoList.size();
      }

      @Override
      public Object getItem(int position) {
          return resultDaoList.get(position);
      }

      @Override
      public long getItemId(int position) {
          return position;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
          if (convertView == null){
              convertView = inflater.inflate(R.layout.item_result,null);
              myViewHolder = new MyVidewHolder();
              myViewHolder.tvItemtvResult = (TextView) convertView.findViewById(R.id.tvItemResult);
              convertView.setTag(myViewHolder);
          }else {
              myViewHolder = (MyVidewHolder) convertView.getTag();
          }
          SimpleDateFormat sdr1 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
          String CreatedTime1 = sdr1.format(new Date(resultDaoList.get(position).getCreatTime()));
          myViewHolder.tvItemtvResult.setText("游戏用时："+resultDaoList.get(position).getUseTime()+"    游戏开始时间为："+CreatedTime1);
          return convertView;
      }

      class MyVidewHolder{
          TextView  tvItemtvResult;
      }
  }
}
