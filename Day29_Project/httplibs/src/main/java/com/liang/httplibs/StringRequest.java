package com.liang.httplibs;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class StringRequest extends Request<String>{
    public StringRequest(String url, Method method, CallBack<String> callBack) {
        super(url, method, callBack);
    }


    @Override
    public void dispatcherContent(byte[] content) {
        onResponse(new String(content));
    }
}
