package com.example;

import java.util.HashMap;

public class Request {

    private String url;
    private Method method;
    private CallBack callBack;
    //枚举GET和POST方法
    public enum Method {
        GET, POST
    }

    public Request(String url, Method method, CallBack callBack) {
        this.url = url;
        this.method = method;
        this.callBack = callBack;
    }

    public String getUrl() {
        return url;
    }

    public Method getMethod() {
        return method;
    }

    public CallBack getCallBack() {
        return callBack;
    }

     public interface CallBack {
        public void onError(Exception e);

        public void onResponse(String result);
    }

    //Post解析传入参数
    public HashMap<String,String>  getParams(){

        return null;
    }
}
