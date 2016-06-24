package com.liang.day29_project.collect_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class DBUtils {
    private static MySQLiteHelper helper;
    private static DBUtils db ;
    public static final String TABLE_COLLECTION = "collection";
    public static final String TABLE_HISTORY = "history";
    private MySQLiteHelper getDBHelper(Context context) {
        if (helper == null)
            synchronized (DBUtils.class) {
                if (helper == null) {
                    helper = new MySQLiteHelper(context);
                }
            }
        return helper;
    }

    private DBUtils(Context context) {
        helper = getDBHelper(context);
    }

    public static DBUtils getDBInstance(Context context){
        if (db == null)
            synchronized (DBUtils.class) {
                if (db == null) {
                    db = new DBUtils(context);
                }
            }

        return db ;
    }

    //插入数据
    public long insert(String table, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.insert(table, null, values);
    }

    //查询数据,全部查询
    public Cursor query(String table) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.query(table, null, null, null, null, null, null, null);
    }

    //按字段查询
    // SELECT * FROM student WHERE 字段1 = 值1, 字段2 = 值2
    public Cursor queryByAColumns
    (String table, String[] columns, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.query(table, columns, selection, selectionArgs, null, null, null);
    }

    //按条件删除
    public int delete(String table, String where, String[] args) {
        SQLiteDatabase db = helper.getWritableDatabase();

        return db.delete(table, where, args);


    }
}
