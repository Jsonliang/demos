package com.liang.day29_project.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class Constant {
    private static final String PRE_FIRSTOPENActivity = "PRE_FIRSTOPENActivity";

    public static final String path = "http://www.tngou.net/api/lore/classify";
    public static final String path_list ="http://www.tngou.net/api/lore/list?id=";
    public static final String path_img = "http://tnfs.tngou.net/image";
    public static final String path_content = "http://www.tngou.net/api/lore/show?id=";


    public static  boolean getConfig(Context context){
       SharedPreferences share =  context.getSharedPreferences("PRE_CONFIG", Context.MODE_PRIVATE);
        return  share.getBoolean(Constant.PRE_FIRSTOPENActivity,true);
    }
    public static boolean putConfig(Context context,boolean value){
        SharedPreferences share =  context.getSharedPreferences
                ("PRE_CONFIG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(Constant.PRE_FIRSTOPENActivity, value);
        return  editor.commit();
    }
}
