package com.liang.day29_project.apps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.liang.day29_project.R;

public class MoreActivity extends AppCompatActivity  implements View.OnClickListener{
    private TextView more_collect ;//收藏夹
    private TextView more_view ;   // 浏览历史
    private TextView about_banben ;//软件版本
    private TextView about_option ;//意见反馈

    private Intent intent  = null ;
    private Toolbar bar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_more);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        bar = (Toolbar) findViewById(R.id.more_toolbar);
        bar.setLogo(R.mipmap.ic_launcher);
        bar.setTitle("Healthy");
    }

    private void initView() {
        more_collect = (TextView) findViewById(R.id.more_collect);
        more_view = (TextView) findViewById(R.id.more_collect_v);
        about_banben = (TextView) findViewById(R.id.more_about_banben);
        about_option = (TextView) findViewById(R.id.more_about_yijian);
        more_collect.setOnClickListener(this);
        more_view.setOnClickListener(this);
        about_banben.setOnClickListener(this);
        about_option.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.more_collect:
                intent = new Intent(this,CollectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.more_collect_v:
                intent = new Intent(this,HistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.more_about_banben:
                 intent = new Intent(this,AboutActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
                break;
            case R.id.more_about_yijian:
                 intent = new Intent(this,FeedbackActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
                break;
        }
    }
}
