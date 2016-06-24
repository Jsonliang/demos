package com.liang.httplibs;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class RequestQueue {
    //新建一个阻塞队列
    public BlockingDeque<Request>  mQueue = new LinkedBlockingDeque<>();

    //后台创建总是最大为3个线程
    private static final int MAX = 3;
    private NetWorkDispatcher[] workers = new NetWorkDispatcher[MAX];

    public RequestQueue(){
        initWorker();
    }

    private void initWorker() {
        for(int i=0 ;i<MAX ;i++){
            workers[i] = new NetWorkDispatcher(mQueue);
            workers[i].start();//启动阻塞的线程
        }
    }

    public void stop(){
        for(int i=0 ;i<MAX ;i++){
            workers[i].interrupt() ;//打断线程
        }
    }

    //加入请求对象
    public void addQuest(Request request){
        mQueue.add(request);
    }
}

