package com.liang.httplibs;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class NetWorkDispatcher extends Thread {

    //传入的一个阻塞队列
    private BlockingDeque<Request> mQueue = null;
    private boolean flag = true;

    public NetWorkDispatcher(BlockingDeque<Request> mQueue) {
        this.mQueue = mQueue;
    }

    @Override
    public void run() {
        while (flag && !interrupted()) {
            try {
                Request req = mQueue.take();
                if (req == null) {
                    throw new IllegalArgumentException("request object is null");
                }
                try {
                    byte[] result = getStringFromNet(req);

                    if (result != null) {
                        //如果返回接口不为null，有接口回调得到结果
                        req.dispatcherContent(result);
                    }
                } catch (Exception e) {
                    //抛出异常也返回给调用者
                    req.getCallBack().onError(e);
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                flag = false ;//线程打断之后不能在继续运行
                e.printStackTrace();
            }

        }
    }

    /**
     * 判断Request对象的URL地址是否为空,以及调用者使用哪种请求方式
     * @param request
     * @return
     * @throws Exception
     */
    private byte[] getStringFromNet(Request request) throws Exception {
        if (TextUtils.isEmpty(request.getUrl())) {
            throw new IllegalAccessException("url is null");//url地址为空
        }
        if (Request.Method.GET == request.getMethod()) {
            return getResponseByGet(request.getUrl());
        } else {
            //POST方式访问数据库
            System.out.println("到了Method方法xxxx");
            return getResponseByPost(request.getUrl(),getEncodeString(request));
        }
    }

    /**
     * 以GET方式请求访问Http
     * @param urlStr 传入的请求对象
     * @return 以字符串方式返回http响应
     * @throws Exception 向外抛出异常，如Http请求异常，IO异常
     */
    private byte[] getResponseByGet(String urlStr) throws Exception {

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式.GET请求方式。拼接的URL地址不要超过1KB
        conn.setRequestMethod("GET");
       // conn.setReadTimeout(5000);
        conn.connect();
        int code = conn.getResponseCode();
        System.out.println();
        if (code != 200) {
            throw new Exception("return code is " + code);//如果网络请求失败，返回对应的请求码
        }

        InputStream is = conn.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        //关闭输入流
        is.close();
        //以UTF-8编码形式返回字符串
        return bos.toByteArray();
    }

    /**
     * 以Post的方式访http网络，并返回解析到服务器返回的信息
     * @param urlStr URL地址
     * @param length 数据长度
     * @return  返回byte数组
     * @throws Exception
     */
    private byte[] getResponseByPost(String urlStr,String length) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式
        conn.setRequestMethod("POST");
       // conn.setReadTimeout(5000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);//不使用缓存
        //以表单的形式提交
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        //获得请求参数长度
        byte[] content =null ;
       if(length!=null){
           content = (urlStr +length).getBytes();
           conn.setRequestProperty("Content-Length",content.length+"");

       }else {
           content = urlStr.getBytes();
           conn.setRequestProperty("Content-Length",content.length+"");
       }


        OutputStream write = conn.getOutputStream();
        write.write(content);//上传数据到服务器


        int code = conn.getResponseCode();
        if (code != 200) {
            throw new Exception("Http parse failure return code is " + code);//如果网络请求失败，返回对应的请求码
        }

        InputStream is = conn.getInputStream();//读取服务器返回的数据
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
            bos.flush();
        }
        //关闭输入流
        is.close();
        //以UTF-8编码形式返回字符串

        return bos.toByteArray();
    }

    /**
     * 得到POST请求方式，参数拼接的字符串
     * @param request
     * @return
     */
    private String getEncodeString(Request request) {
        HashMap<String, String> params = request.getParams();
        if (params != null) {
            StringBuffer buff = new StringBuffer();
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            int index = 0;
            while (iterator.hasNext()) {
                if (index > 0) {
                    buff.append("&");
                }
                Map.Entry<String, String> value = iterator.next();
                String str = value.getKey() + "=" + value.getValue();
                buff.append(str);
                index++;
            }

            return buff.toString();
        }

        return null;
    }
}
