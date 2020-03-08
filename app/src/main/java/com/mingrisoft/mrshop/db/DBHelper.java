package com.mingrisoft.mrshop.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者： LYJ
 * 功能： 创建数据库
 * 创建日期： 2017/5/3
 */

public class DBHelper extends SQLiteOpenHelper{
    private static volatile DBHelper dbHelper;
    private static final String DB_NAME = "Shop_db";//数据库的名称
    private static final int VERSION_CODE = 1;//数据库的版本号
    private DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION_CODE);
    }

    /**
     * 获取单例 返回数据库操作对象
     * @param context
     * @return
     */
    public static DBHelper getInstance(Context context){
        if (null == dbHelper){
            synchronized (DBHelper.class){
                if (null == dbHelper){
                    dbHelper = new DBHelper(context.getApplicationContext());
                }
            }
        }
        return dbHelper;
    }

    /**
     * 创建数据库
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //购物车数据数据表,主键 商品ID、商品标题、商品价格、商品品牌、图片的保存路径、商品数量、图片、店铺
        String cartTable = "create table t_cart( _id varchar(10) primary key, " +
                "title text, price real , brand varchar(20), image_url text," +
                "count integer, image text, merchant varchar(20));";
        db.execSQL(cartTable);
    }

    /**
     * 更新数据库
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
