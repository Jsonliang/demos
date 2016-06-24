package com.liang.httplibs;

import java.util.HashMap;

public abstract class Request<T> {

    private String url;
    private Method method;
    private CallBack<T> callBack;
    //枚举GET和POST方法
    public enum Method {
        GET, POST
    }

    public Request(String url, Method method, CallBack<T> callBack) {
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

    public void onError(Exception e){
        callBack.onError(e);
    }
    protected void onResponse(T res){
        callBack.onResponse(res);
    }
     public interface CallBack<T> {
         void onError(Exception e);
         void onResponse(T result);
    }

    //Post解析传入参数
    public HashMap<String,String>  getParams(){

        return null;
    }

    public abstract void dispatcherContent(byte[] content);
}
