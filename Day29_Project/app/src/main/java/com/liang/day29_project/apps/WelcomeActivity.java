package com.liang.day29_project.apps;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.liang.day29_project.R;
import com.liang.day29_project.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    private int[] resources = new int[]{R.drawable.slide1,R.drawable.slide2,
    R.drawable.slide3};

    private List<ImageView> images = new ArrayList<ImageView>();
    private ViewPager vp;
    private LinearLayout ll_container;
    private MyAdapter adapter;
    private int pre_index = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.welcome_vp);
        ll_container = (LinearLayout) findViewById(R.id.welcome_ll);
        adapter = new MyAdapter();
        initImages();
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(adapter);

    }

    private void initImages() {
        ImageView vp_image = null;
        ImageView ll_image = null;
        LinearLayout.LayoutParams iv_param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams vp_param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < resources.length; i++) {
            vp_image = new ImageView(this);
            vp_image.setImageResource(resources[i]);
            vp_image.setScaleType(ImageView.ScaleType.FIT_XY);
            vp_image.setLayoutParams(vp_param);
            images.add(vp_image);

            ll_image = new ImageView(this);
            ll_image.setScaleType(ImageView.ScaleType.FIT_XY);
            if (i == 0) {
                ll_image.setBackgroundResource(R.drawable.page_now);
            } else {
                ll_image.setBackgroundResource(R.drawable.page);
            }
            iv_param.rightMargin =  5 ;
            ll_image.setLayoutParams(iv_param);
            ll_container.addView(ll_image);
        }
    }

    class MyAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener, View.OnClickListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            ll_container.getChildAt(position).setBackgroundResource(R.drawable.page_now);
            ll_container.getChildAt(pre_index).setBackgroundResource(R.drawable.page);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(images.get(position));
            if(position == images.size() -1){
                images.get(position).setOnClickListener(this);
            }


            return images.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(WelcomeActivity.this,LoadingActivity.class);
            startActivity(intent);
            //保存sharepreference为false
            Constant.putConfig(WelcomeActivity.this,false);
            finish();
        }
    }
}
