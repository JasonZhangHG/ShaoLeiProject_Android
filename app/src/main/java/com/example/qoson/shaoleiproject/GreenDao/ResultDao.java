package com.example.qoson.shaoleiproject.GreenDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by qoson on 2017/3/24.
 */
@Entity // 标识实体类，greenDAO会映射成sqlite的一个表，表名为实体类名的大写形式
public class ResultDao {
    @Id(autoincrement = false)
    public long CreatTime;
    @Property(nameInDb = "DBBeanUserInfoFansName")
    public int useTime;  // 记录成功耗时
    @Generated(hash = 594067346)
    public ResultDao(long CreatTime, int useTime) {
        this.CreatTime = CreatTime;
        this.useTime = useTime;
    }
    @Generated(hash = 699765733)
    public ResultDao() {
    }
    public long getCreatTime() {
        return this.CreatTime;
    }
    public void setCreatTime(long CreatTime) {
        this.CreatTime = CreatTime;
    }
    public int getUseTime() {
        return this.useTime;
    }
    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }

}
