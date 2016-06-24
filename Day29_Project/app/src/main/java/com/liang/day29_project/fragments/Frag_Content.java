package com.liang.day29_project.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liang.day29_project.R;
import com.liang.day29_project.adapters.C_InfoListAdapter;
import com.liang.day29_project.apps.ContentActivity;
import com.liang.day29_project.beans.Info;
import com.liang.day29_project.utils.Constant;
import com.liang.httplibs.HttpHelper;
import com.liang.httplibs.Request;
import com.liang.httplibs.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class Frag_Content extends Fragment {
    private ListView mList;
    private int class_Id;
    private PtrClassicFrameLayout listFresh;
    private C_InfoListAdapter adapter;
    private List<Info> infos  = null ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.frag_content, container, false);
        class_Id =  getArguments().getInt("id");//获取传过来的id
        initView(view);
        infos = new ArrayList<Info>();
        if(bundle !=null){
            Parcelable[]  parcel = bundle.getParcelableArray("cache");
            Info[] cache = (Info[]) bundle.getParcelableArray("cache");
            if(cache !=null && cache.length != 0){
                infos = Arrays.asList(cache);
                adapter = new C_InfoListAdapter(infos);
                mList.setAdapter(adapter);
            }else {
                //下载数据
                getDataFromNet();
            }
        }else {
            //下载数据
            getDataFromNet();
        }


        return view;
    }

    private void initView(View view) {
        mList = (ListView) view.findViewById(R.id.frg_content_lv);
        listFresh = (PtrClassicFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
        listFresh.setResistance(1.7f);
        listFresh.setRatioOfHeaderHeightToRefresh(1.2f);
        listFresh.setDurationToClose(200);
        listFresh.setDurationToCloseHeader(1000);
        // default is false
        listFresh.setPullToRefresh(true);
        // default is true
        listFresh.setKeepHeaderWhenRefresh(true);
        listFresh.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //开始下载网络数据
                getDataFromNet();

            }
        });

        //设置listView的单击事件
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 int show_id = infos.get(position).getId();
                Intent intent = new Intent(getActivity(),ContentActivity.class);
                intent.putExtra("show_id",show_id);
                startActivity(intent);
            }
        });
    }

    /**
     * 从网络下载数据
     */
    private void getDataFromNet() {
        String url = Constant.path_list + class_Id ;
        //System.out.println(url+"");
        StringRequest request = new StringRequest(url, Request.Method.GET, new Request.CallBack<String>() {
            @Override
            public void onError(Exception e) {
            }
            @Override
            public void onResponse(String result) {
               //解析JSON字符串
             //   System.out.println(result+"++++++++++++++++");
                List<Info> list = parseJson2List(result);


                if(list != null && list.size() != 0){
                    infos.clear();
                    infos.addAll(list);

                    if(adapter == null){
                        adapter = new C_InfoListAdapter(infos);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mList.setAdapter(adapter);
                            }
                        });
                    }else {
                        //唤醒adapter更新数据
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                }
                //刷新完成
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listFresh.refreshComplete();
                    }
                });
            }
        });

        //添加请求对象到队列
        HttpHelper.addRequest(request);
    }

    /**
     * Json字符串转为 list集合
     */
    private List<Info> parseJson2List(String str) {
        List<Info> list = null;
        try {
            JSONObject object = new JSONObject(str);
            JSONArray array = object.getJSONArray("tngou");
            JSONObject json = null ;
            Info info = null ;
            list = new ArrayList<Info>();
            for(int i =0 ;i <array.length(); i++){
                json = array.getJSONObject(i);
                info = new Info();
                info.setDesc(json.optString("description"));
                info.setId(json.optInt("id"));
                info.setRcount(json.optInt("rcount"));
                info.setImg(json.optString("img"));
                info.setTime(json.optString("time"));
                list.add(info);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list ;
    }

   @Override
    public void onSaveInstanceState(Bundle outState) {
        if(infos == null || infos.size() == 0) return ;
        Info[] parcel = new Info[infos.size()];
        infos.toArray(parcel);//把infos集合转成数组
        outState.putParcelableArray("cache",parcel);
    }
}

