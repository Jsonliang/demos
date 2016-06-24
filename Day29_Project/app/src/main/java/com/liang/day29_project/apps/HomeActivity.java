package com.liang.day29_project.apps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.liang.day29_project.R;
import com.liang.day29_project.beans.TableInfo;
import com.liang.day29_project.fragments.Frag_Content;
import com.liang.day29_project.utils.Constant;
import com.liang.httplibs.HttpHelper;
import com.liang.httplibs.Request;
import com.liang.httplibs.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    private  final String TAG = getClass().getSimpleName();

    private ViewPager home_vp;
    private TabLayout home_tab;
    private List<TableInfo> tableInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        initActionBar();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // shareSDK 释放资源
       // ShareSDK.stopSDK(this);
    }

    private void initActionBar() {
        Toolbar bar = (Toolbar) findViewById(R.id.home_toolbar);
        bar.setNavigationIcon(R.mipmap.ic_launcher);
        bar.setTitle("Healthy");
        findViewById(R.id.h_toolbar_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,
                        MoreActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {

        home_vp = (ViewPager) findViewById(R.id.home_viewpager);
        home_tab = (TabLayout) findViewById(R.id.home_tablayout);

        //访问网络获取导航条信息
        HttpHelper.addRequest(new StringRequest(Constant.path, Request.Method.GET,
                new Request.CallBack<String>() {
                    @Override
                    public void onError(Exception e) {

                    }
                    @Override
                    public void onResponse(final String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tableInfo = new ArrayList<TableInfo>();
                                try {
                                    parseJson2List(result);//获得tableInfo集合的值
                                    if(tableInfo.size()!=0){}
                                    home_vp.setAdapter(new ContentAdapter
                                            (getSupportFragmentManager()));
                                    home_tab.setupWithViewPager(home_vp);
                                    home_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }));
    }

    private void parseJson2List(String res) throws JSONException {
        JSONObject object = new JSONObject(res);
        JSONArray array = object.getJSONArray("tngou");
        TableInfo tab = null;
        JSONObject  json = null;
        for(int i =0 ;i < array.length() ;i++){
            json = array.getJSONObject(i);
            String name = json.optString("name");
            int class_Id = json.optInt("id");
            tab = new TableInfo(name,class_Id);
            tableInfo.add(tab);
        }
    }
    private List<Frag_Content> fragments = null ;
    Frag_Content fragment = null ;
    class ContentAdapter extends FragmentStatePagerAdapter {

        public ContentAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<Frag_Content>();
            for(int i = 0 ;i <tableInfo.size() ;i++){
                fragments.add(null);
            }
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putInt("id", tableInfo.get(position).getClass_Id());

            fragment = new Frag_Content();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return tableInfo.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tableInfo.get(position).getTitle();
        }
    }
}
