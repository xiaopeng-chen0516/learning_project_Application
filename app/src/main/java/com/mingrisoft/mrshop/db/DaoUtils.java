package com.mingrisoft.mrshop.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： LYJ
 * 功能： 数据操作帮助类
 * 创建日期： 2017/5/3
 */

public class DaoUtils {
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    public DaoUtils(Context context) {
        dbHelper = DBHelper.getInstance(context);//获取数据库操作类
    }

    /**
     * 插入数据
     * @param tableName 表的名称
     * @param values 插入的数据(键值对结构)
     * @return 返回操作结果 成功：OPERATION_FAILURE 失败：OPERATION_SUCCESS
     */
    public int insert(String tableName, ContentValues values){
        database = dbHelper.getWritableDatabase();
        long result = database.insert(tableName,null,values);
        database.close();
        return result == -1 ? DBStateCode.OPERATION_FAILURE : DBStateCode.OPERATION_SUCCESS;
    }

    /**
     * 更新指定表中的数据
     * @param tableName 更改的表名
     * @param values 要更改的数据
     * @param whereClause 筛选条件
     * @param whereArgs 条件
     * @return
     */
    public int update(String tableName, ContentValues values, String whereClause, String[] whereArgs){
        database = dbHelper.getWritableDatabase();
        int result = database.update(tableName,values,whereClause,whereArgs);
        database.close();
        return result == 0 ? DBStateCode.OPERATION_FAILURE : DBStateCode.OPERATION_SUCCESS;
    }

    /**
     * 删除表中符合条件的数据
     * @param tableName 表名
     * @param whereClause 筛选条件
     * @param whereArgs 条件
     * @return
     */
    public int delete(String tableName, String whereClause, String[] whereArgs){
        database = dbHelper.getWritableDatabase();
        int result = database.delete(tableName,whereClause,whereArgs);
        database.close();
        return result == 0 ? DBStateCode.OPERATION_FAILURE : DBStateCode.OPERATION_SUCCESS;
    }

    public int deleteAll(String tableName){
        database = dbHelper.getWritableDatabase();
        int result = database.delete(tableName,"",null);
        database.close();
        return result == 0 ? DBStateCode.OPERATION_FAILURE : DBStateCode.OPERATION_SUCCESS;
    }

    /**
     * 查询数据（需手动关闭数据库连接）
     * @param tableName
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    public Cursor getResultCursor(String tableName, String[] columns, String selection,
                                  String[] selectionArgs, String groupBy, String having,
                                  String orderBy){
        database = dbHelper.getWritableDatabase();
        return database.query(tableName,columns,selection,selectionArgs,groupBy,having,orderBy);
    }
    /**
     * 全查询（需手动关闭数据库连接）
     * @param tableName
     * @return
     */
    public Cursor getResultCursorAll(String tableName){
        return getResultCursor(tableName,null,null,null,null);
    }

    /**
     * 条件查询（需手动关闭数据库连接）
     * @param tableName
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param orderBy
     * @return
     */
    public Cursor getResultCursor(String tableName, String[] columns, String selection,
                                  String[] selectionArgs,String orderBy){
        return getResultCursor(tableName,columns,selection,selectionArgs,null,null,orderBy);
    }
    /**
     * 关闭数据库连接
     */
    public void closedDB(){
        if (database.isOpen())database.close();
    }

    /**
     * 返回指定类型的数据集合
     * @param tableName
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getResultListAll(String tableName,Class<T> clazz){
        List<T> list = new ArrayList<>();
        Cursor cursor = getResultCursorAll(tableName);
        try {
            while (cursor.moveToNext()){
                //创建指定类型的对象
                T obj = clazz.newInstance();
                Class<?> tClass = obj.getClass();
                int length = cursor.getColumnCount();//获取列数
                for (int i = 0;i < length;i++){
                    String ColumnName = cursor.getColumnName(i);//获取列名
                    //根据列名给对象的指定属性赋值
                    Field field = tClass.getDeclaredField(ColumnName);//获取指定的属性
                    setValuesToObject(field,obj,cursor,i);
                }
                list.add(obj);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        cursor.close();
        closedDB();
        return list;
    }

    /**
     * 设置指定类型的属性值
     * @param field 反射的数据
     * @param object 要生成的对象
     * @param cursor 游标
     * @param index 索引
     */
    private void setValuesToObject(Field field,Object object,Cursor cursor,int index) throws IllegalAccessException {
        String type = field.getType().getSimpleName();//类型
        field.setAccessible(true);//解锁私有属性
        switch (type){//根据类型设置属性值
            case "String":
                field.set(object,cursor.getString(index));
                break;
            case "int":
                field.setInt(object,cursor.getInt(index));
                break;
            case "long":
                field.setLong(object,cursor.getLong(index));
                break;
            case "double":
                field.setDouble(object,cursor.getDouble(index));
                break;
            case "float":
                field.setFloat(object,cursor.getFloat(index));
                break;
        }
    }
}
