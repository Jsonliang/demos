package com.liang.day29_project.apps;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.TextView;

import com.liang.day29_project.R;

public class AboutActivity extends AppCompatActivity {
    private TextView tv_version ;
    private Toolbar bar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        bar = (Toolbar) findViewById(R.id.about_toolbar);
        bar.setLogo(R.mipmap.ic_launcher);
        bar.setTitle("Healthy");
        bar.inflateMenu(R.menu.item_toolbar);


    }

    private void initView() {

        tv_version = (TextView) findViewById(R.id.about_version);
        tv_version.setText("version: "+ getVersion());
    }
/*    PackageManager manager = this.getPackageManager();
    8         PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
    9         String version = info.versionName;
    10         return this.getString(R.string.version_name) + version;*/

    private String getVersion(){
       try {
           //打开包管理
           PackageManager pm = this.getPackageManager();
           //获得应用包的信息
           PackageInfo info = pm.getPackageInfo(getApplicationContext().getPackageName(),0);
           String version = info.versionName;//获得版本名

           return version ;
       }catch(Exception e){

       }

        return null;
    }
}
