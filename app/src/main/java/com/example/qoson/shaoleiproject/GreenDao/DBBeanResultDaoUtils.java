package com.example.qoson.shaoleiproject.GreenDao;

import android.content.Context;

import com.aidebar.greendaotest.gen.DaoManager;
import com.aidebar.greendaotest.gen.ResultDaoDao;

import java.util.List;


/**
 * Created by qoson on 2017/3/24.
 */

public class DBBeanResultDaoUtils {

    private ResultDaoDao resultDaoDao ;
    private static DBBeanResultDaoUtils dbBeanResultDaoUtils = null;

    public static DBBeanResultDaoUtils getInstance(){
        return  dbBeanResultDaoUtils ;
    }
    public static void Init(Context context){
        if( dbBeanResultDaoUtils  == null){
            dbBeanResultDaoUtils  = new DBBeanResultDaoUtils(context);
        }
    }

    public DBBeanResultDaoUtils  (Context context){
        resultDaoDao = DaoManager.getInstance(context).getNewSession().getResultDaoDao();
    }
    /**
     * 完成对数据库中插入一条数据操作
     * @param resultDao
     * @return
     */
    public void insertOneData(ResultDao resultDao){
        resultDaoDao.insertOrReplace(resultDao);
    }

    /**
     * 完成对数据库中插入多条数据操作
     * @param  resultDaoList
     * @return
     */
    public boolean insertManyData(List<ResultDao> resultDaoList){
        boolean flag = false;
        try{
            resultDaoDao.insertOrReplaceInTx( resultDaoList);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库中删除一条数据操作
     * @param resultDao
     * @return
     */
    public boolean deleteOneData(ResultDao resultDao){
        boolean flag = false;
        try{
            resultDaoDao.delete(resultDao);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库中删除一条数据 ByKey操作
     * @return
     */
    public boolean deleteOneDataByKey(long id){
        boolean flag = false;
        try{
            resultDaoDao.deleteByKey(id);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库中批量删除数据操作
     * @return
     */
    public boolean deleteManData(List<ResultDao> resultDaoList){
        boolean flag = false;
        try{
            resultDaoDao.deleteInTx(resultDaoList);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库更新数据操作
     * @return
     */
    public boolean updateData(ResultDao resultDao){
        boolean flag = false;
        try{
            resultDaoDao.update(resultDao);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库批量更新数据操作
     * @return
     */
    public boolean updateManData(List<ResultDao> resultDaoList){
        boolean flag = false;
        try{
            resultDaoDao.updateInTx(resultDaoList);
            flag = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 完成对数据库查询数据操作
     * @return
     */
    public ResultDao queryOneData(long id) {
        return resultDaoDao.load(id);
    }

    /**
     * 完成对数据库查询所有数据操作
     * @return
     */
    public List<ResultDao> queryAllData() {
        return resultDaoDao.loadAll();
    }
}
