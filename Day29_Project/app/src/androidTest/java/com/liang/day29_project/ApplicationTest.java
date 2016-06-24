package com.liang.day29_project;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
/*


    public void testdb() throws  Exception{
        String db_1 = "collection";
        String db_2 = "history";
        String str ="http://www.tngou.net/api/lore/show?id=19623";
        String title = "夏季吃什么能瘦腰？ 推荐5款常见食物";

        DBUtils utils = DBUtils.getDBInstance(getContext());
        ContentValues values1 = new ContentValues();
        values1.put("path",str);
        values1.put("title",title);
        utils.insert("collection",values1);

        ContentValues values2 = new ContentValues();
        values2.put("path",str);
        values2.put("title",title);
        utils.insert("history",values2);

    }

    public void testQuery(){
        DBUtils utils = DBUtils.getDBInstance(getContext());
        Cursor cursos = utils.query("collection");
  */
/*      int index = cursor.getColumnIndex("name"); // 获得name字段所在的索引号
        String name = cursor.getString(index); // *//*

        while(cursos.moveToNext()){
            int index = cursos.getColumnIndex("path");
            String path = cursos.getString(index);
            System.out.println(path);
            index = cursos.getColumnIndex("title");
            String title = cursos.getString(index);
            System.out.println(title);
        }
    }
*/

    public void testTime(){
        long time = System.currentTimeMillis();
        String t = time+"";


      //  System.out.println(str+"bgihjgihohboi");
    }

    public void testPost() throws Exception {
       String  path =
               "http://10.16.152.32:8080/CBK_Feedback/Feedback?";
        TreeMap<String,String> params = new TreeMap<>();
        params.put("title","123");
        params.put("text","456");
        String len = getEncodeString(params);
        getResponseByPost(path,len);
    }



    private byte[] getResponseByPost(String urlStr,String length) throws Exception {
       System.out.println(urlStr+length +"xxxxxx");
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式
        conn.setRequestMethod("POST");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);//不使用缓存
        //以表单的形式提交conn.setRequestProperty("Accept-Charset", "UTF-8");
       // conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //获得请求参数长
        byte[] content = null ;
        if (length != null) {
            content = length.getBytes();
            conn.setRequestProperty("Content-Length", content.length + "");

        }else{
            content = urlStr.getBytes();
            conn.setRequestProperty("Content-Length", content.length + "");
        }
        OutputStream write = conn.getOutputStream();
        write.write(content);//上传数据到服务器
        System.out.println("写入数据成功");
        int code = conn.getResponseCode();
        if (code != 200) {
            System.out.println("xxxxx");
            throw new Exception("Http parse failure return code is " + code);//如果网络请求失败，返回对应的请求码
        }

        InputStream is = conn.getInputStream();//读取服务器返回的数据
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        System.out.println("chengogng1duao");
        //关闭输入流
        is.close();
        //以UTF-8编码形式返回字符串
        System.out.println(bos.toString()+"xxxxx");
        return bos.toByteArray();
    }

    private String getEncodeString(TreeMap<String,String> params) {
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
        System.out.println(buff.toString()+"params+++");
            return buff.toString();
        }

        return null;
    }
}