package com.liang.day29_project.apps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.liang.day29_project.R;
import com.liang.day29_project.utils.Constant;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {
    Timer timer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);
        init();

    }

    private void init() {
        if(Constant.getConfig(this)){
            //第一次打开
            Intent intent = new Intent(this,WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //3秒后启动到HomeActivity,并且关闭自已
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                cancel();
            }
        },2000);
    }
}
