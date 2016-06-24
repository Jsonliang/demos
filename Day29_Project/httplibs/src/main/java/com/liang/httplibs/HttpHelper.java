package com.liang.httplibs;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class HttpHelper {
    private static RequestQueue mQueue ;


    private HttpHelper(){}
    private static RequestQueue getInstance(){
        if(mQueue == null){
            synchronized (HttpHelper.class){
                if(mQueue == null){
                    mQueue = new RequestQueue();
                }
            }
        }
        return mQueue;
    }

    public static void addRequest(Request request){
        getInstance();
        mQueue.addQuest(request);
    }
}
