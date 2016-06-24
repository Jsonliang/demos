package com.liang.day29_project.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class Info implements Parcelable{
   private int id ;
    private String desc ;
    private int rcount ;
    private String img ;
    private String time;

    public Info() {
    }
    public Info(int id, String desc, int rcount, String img ,String time) {
        this.id = id;
        this.desc = desc;
        this.rcount = rcount;
        this.img = img;
        this.time = time ;
    }

    protected Info(Parcel in) {
        id = in.readInt();
        desc = in.readString();
        rcount = in.readInt();
        img = in.readString();
        time = in.readString();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(desc);
        dest.writeInt(rcount);
        dest.writeString(img);
        dest.writeString(time);

    }

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", rcount=" + rcount +
                ", img='" + img + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
