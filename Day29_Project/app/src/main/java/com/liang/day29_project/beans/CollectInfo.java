package com.liang.day29_project.beans;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class CollectInfo {
    private int id ;
    private String path ;
    private String title ;

    private String img ;
    private String share_path ;


    public CollectInfo() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShare_path() {
        return share_path;
    }

    public void setShare_path(String share_path) {
        this.share_path = share_path;
    }

    @Override
    public String toString() {
        return "CollectInfo{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
