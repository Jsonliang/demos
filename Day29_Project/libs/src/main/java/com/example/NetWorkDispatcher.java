package com.example;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingDeque;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class NetWorkDispatcher extends Thread {

    //传入的一个阻塞队列
    private BlockingDeque<Request> mQueue = null;
    public boolean flag = true;

    public NetWorkDispatcher(BlockingDeque<Request> mQueue) {
        this.mQueue = mQueue;
    }

    @Override
    public void run() {
        while (flag && !interrupted()) {
            try {
                Request req = mQueue.take();
                try {
                    String result = getResponseByGet(req);
                     if(result == null ){
                         //如果返回接口不为null，有接口回调得到
                         req.getCallBack().onResponse(result);
                     }
                } catch (Exception e) {
                    //抛出异常也返回给调用者
                    req.getCallBack().onError(e);
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 以GET方式请求访问Http
     * @param request 传入的请求对象
     * @return 以字符串方式返回http响应
     * @throws Exception 向外抛出异常，如Http请求异常，IO异常
     */
    private String getResponseByGet(Request request) throws Exception {

        if(request.getUrl() == null){
            throw new IllegalAccessException("url is null");//url地址为空
        }
        URL url = new URL(request.getUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式
        conn.setRequestMethod(request.getMethod() ==
                Request.Method.GET ? "GET" : "POST");
        conn.setReadTimeout(5000);
        conn.connect();
        int code = conn.getResponseCode();
        if (code != 200) {
            throw new Exception("return code is "+ code);//如果网络请求失败，返回对应的请求码
        }

        InputStream is = conn.getInputStream() ;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len ;
        byte[] buffer = new byte[1024];
        while((len = is.read(buffer)) != -1){
            bos.write(buffer,0 ,len);
        }
        //关闭输入流
        is.close();
        //以UTF-8编码形式返回字符串
        return bos.toString(Charset.defaultCharset().name());
    }

    /**
     * 以Post的方式访http网络，并返回解析到的网络信息
     * @param request
     * @return
     * @throws Exception
     */
    private String getResponseByPost(Request request) throws Exception{
        return "";
    }
}
