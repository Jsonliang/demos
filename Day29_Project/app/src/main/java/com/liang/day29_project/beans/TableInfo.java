package com.liang.day29_project.beans;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class TableInfo {
    public int class_Id ;
    public String title ;

    public TableInfo(String title,int class_Id) {
        this.class_Id = class_Id;
        this.title = title;
    }

    public int getClass_Id() {
        return class_Id;
    }

    public String getTitle() {
        return title;
    }
}
