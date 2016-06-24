package com.liang.day29_project.apps;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liang.day29_project.R;
import com.liang.day29_project.collect_data.DBUtils;
import com.liang.day29_project.utils.Constant;
import com.liang.httplibs.HttpHelper;
import com.liang.httplibs.Request;
import com.liang.httplibs.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private TextView keyWord;
    private TextView time;
    private WebView detail_web ;

    private Toolbar bar;
    private ImageView iv_shared;
    private ImageView iv_collect;
    private int id;

    private String db_path;
    private String db_title;//保存到数据库的title  和 分享的title
    private boolean isCollect = false;
    private DBUtils db;

    private String share_path ;//分享的连接
    private String share_img ;//分享的图片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_content);

        initView();

        //初始化 ShareSDK ;
        ShareSDK.initSDK(this);
    }

    protected void insert() {
        //添加浏览记录
        if (db_title != null && db_path != null) {
            db = DBUtils.getDBInstance(getApplicationContext());
            ContentValues values = new ContentValues();
            values.put("path", db_path);
            values.put("title", db_title);
            db.insert("history", values);
        }

        //查询查询数据库 是否已经收藏
        new Thread(new Runnable() {
            @Override
            public void run() {
                db = DBUtils.getDBInstance(getApplicationContext());
                final Cursor cursor = db.queryByAColumns(DBUtils.TABLE_COLLECTION, new String[]{"path"}, "path = ?"
                        , new String[]{db_path});

                //查询结束设置回收藏按钮可用,并判断是否已经收藏
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cursor.getCount() != 0) {
                            isCollect = true;
                            //收藏图片变亮
                            iv_collect.setImageResource
                                    (android.R.drawable.btn_star_big_on);
                        } else {
                            isCollect = false;
                        }
                        iv_collect.setEnabled(true);
                        System.out.println("进来了iv设置");
                    }
                });
            }
        }).start();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.content_title);
        keyWord = (TextView) findViewById(R.id.content_kewword);
        time = (TextView) findViewById(R.id.content_time);
        detail_web = (WebView) findViewById(R.id.content_webview);
        bar = (Toolbar) findViewById(R.id.content_toolbar);
        bar.setTitle("Healthy");
        bar.setNavigationIcon(R.mipmap.ic_launcher);

        //分享监听时间
        iv_shared = (ImageView) findViewById(R.id.content_shared);
        iv_shared.setOnClickListener(this);
        iv_shared.setEnabled(false);

        //收藏监听事件
        iv_collect = (ImageView) findViewById(R.id.content_collect);
        iv_collect.setEnabled(false);//先设置不可以单击
        iv_collect.setOnClickListener(this);

        //获取传过来的信息
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("show_id", 0);
            //开启网络下载信息
            getInfoFromNet();
        }
    }

    private void getInfoFromNet() {
        String url = Constant.path_content + id;
        db_path = url;
        StringRequest request = new StringRequest(url, Request.Method.GET, new Request.CallBack<String>() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String result) {
                if (result != null) {
                    parseJSON2List(result);
                }
            }
        });
        HttpHelper.addRequest(request);
    }

    private void parseJSON2List(String str) {
        System.out.println(Thread.currentThread().getName() + "");
        try {
            JSONObject object = new JSONObject(str);
            updateUI(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(final JSONObject object) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                title.setText(object.optString("title"));
                db_title = object.optString("title");
                keyWord.setText(object.optString("keywords"));
                time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                        format(Long.parseLong(object.optString("time"))) + "");
               // msg.setText(Html.fromHtml(object.optString("message")));

                //获得图片连接
                share_img = object.optString("img");
                //获得本页连接
                share_path = object.optString("url");
                detail_web.loadUrl(share_path);
                insert();//添加纪录
                iv_shared.setEnabled(true);//更新完ui后 可以分享
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_collect:
                setCollect();
                break;
            case R.id.content_shared:
                setShareShow();
                break;

        }
    }

    //initSDK是可以重复调用的，其实ShareSDK建议在不确定的时候调用这个方法来
    // 保证ShareSDK被正确初始化。而stopSDK
    // 一旦被调用了，就必须重新调用initSDK才能使用ShareSDK的功能，
    // 否则会出现空指针异常
    private void setShareShow() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

   // title标题：微信、QQ（新浪微博不需要标题）
        oks.setTitle(db_title);  //最多30个字符

   // text是分享文本：所有平台都需要这个字段
        oks.setText(db_title + share_path);  //最多40个字符

    // imagePath是图片的本地路径：除Linked-In以外的平台都支持此参数
        //oks.setImagePath(Environment.getExternalStorageDirectory()
        // + "/meinv.jpg");//确保SDcard下面存在此张图片

        //网络图片的url：所有平台
        oks.setImageUrl(Constant.path_img+share_img);//网络图片rul

        // url：仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(share_path);   //网友点进链接后，可以看到分享的详情
        // Url：仅在QQ空间使用
        oks.setTitleUrl(share_path);  //网友点进链接后，可以看到分享的详情

        // 启动分享GUI
        oks.show(this);
    }

    private void setCollect() {

        if (isCollect) {
            Toast.makeText(ContentActivity.this,
                    "已经收藏过，无需多次收藏", Toast.LENGTH_SHORT).show();
        } else {
            //添加到收藏数据库
            ContentValues values = new ContentValues();
            values.put("path", db_path);
            values.put("title", db_title);
            db = DBUtils.getDBInstance(getApplicationContext());
            long l = db.insert(DBUtils.TABLE_COLLECTION, values);
            if (l > 0) {
                Toast.makeText(ContentActivity.this,
                        "收藏成功", Toast.LENGTH_SHORT).show();
                isCollect = true;
                //图片变颜色
                iv_collect.setImageResource
                        (android.R.drawable.btn_star_big_on);
            }
        }
    }
}
