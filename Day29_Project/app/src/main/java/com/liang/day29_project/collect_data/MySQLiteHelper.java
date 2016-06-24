package com.liang.day29_project.collect_data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    //数据库名字
    private static final String DB_NAME = "data.db";
    private static final int VERSION = 1 ;
    public MySQLiteHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // "CREATE TABLE IF NOT EXISTS [collection]" +
        //        " (  [_id] INTEGER PRIMARY KEY AUTOINCREMENT, [path] TEXT, [title] TEXT);";
        String sql_collect = "CREATE TABLE IF NOT EXISTS [collection] " +
                " ( [_id] INTEGER PRIMARY KEY AUTOINCREMENT, [path] TEXT NOT NULL, [title] TEXT);" ;
        db.execSQL(sql_collect);

        String sql_history = "CREATE TABLE IF NOT EXISTS [history] " +
                " ( [_id] INTEGER PRIMARY KEY AUTOINCREMENT, [path] TEXT NOT NULL , [title] TEXT);";
        db.execSQL(sql_history);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
