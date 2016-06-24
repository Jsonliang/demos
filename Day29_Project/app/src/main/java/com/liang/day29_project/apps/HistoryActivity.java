package com.liang.day29_project.apps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.liang.day29_project.R;
import com.liang.day29_project.beans.CollectInfo;
import com.liang.day29_project.collect_data.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView listView ;
    private List<CollectInfo> infos = new ArrayList<>();
    private CollectInfo info = null ;
    private Toolbar bar ;
    private  HistoryAdapter adapter;
    private DBUtils db ;

    private boolean isDelete = false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history);
        getDataFromDB();
        initView();
        initToolbar();
    }
    private void initToolbar() {
        bar = (Toolbar) findViewById(R.id.history_toolbar);
        bar.setLogo(R.mipmap.ic_launcher);
        bar.setTitle("Healthy");
    }
    private void getDataFromDB() {
        db = DBUtils.getDBInstance(getApplicationContext());
        Cursor cursor = db.query(DBUtils.TABLE_HISTORY);
        if(cursor.getCount() == 0){
            isDelete = true  ;
            return ;
        }
        while(cursor.moveToNext()){
            info = new CollectInfo();
            int index = cursor.getColumnIndex("path");
             info.setPath(cursor.getString(index));
            index = cursor.getColumnIndex("title");
            info.setTitle(cursor.getString(index));
            infos.add(info);
        }
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.history_listview);
        adapter = new HistoryAdapter();
        listView.setAdapter(adapter);
        //设置listvew单击时间
        listView.setOnItemClickListener(adapter);
        //设置listview长按事件
        listView.setOnItemLongClickListener(adapter);

        //清空历史记录
      findViewById(R.id.history_clearall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(HistoryActivity.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("重要消息!");
                builder.setMessage("确定要删除全部历史浏览记录吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isDelete){
                            Toast.makeText(HistoryActivity.this,"浏览记录为空!",Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        delteHistoryFromDB();
                        //重新唤醒adapter
                        infos.clear();
                        getDataFromDB();
                        adapter.notifyDataSetChanged();
                        isDelete = true ;//isDelete为true 不能在删除
                    }
                });
                builder.setNegativeButton("取消",null);
                AlertDialog dialog = builder.create();
                if(!isDelete){
                    dialog.show();
                }

            }
        });
    }
    //清空数据浏览历史
    private void delteHistoryFromDB() {
        db = DBUtils.getDBInstance(getApplicationContext());
        db.delete(DBUtils.TABLE_HISTORY,null,null);
    }



    class HistoryAdapter extends BaseAdapter implements AdapterView.OnItemClickListener
            ,AdapterView.OnItemLongClickListener{

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public CollectInfo getItem(int position) {
            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
           ViewHolder holder = null ;
            if(view == null){
                view = View.inflate(parent.getContext(),R.layout.item_more,null);
                holder = new ViewHolder();
                holder.tv_path = (TextView) view.findViewById(R.id.collect_history_path);
                holder.tv_title = (TextView) view.findViewById(R.id.collect_history_title);

                view.setTag(holder);
            }
             holder = (ViewHolder) view.getTag();

             holder.tv_path.setText(infos.get(position).getPath());
             holder.tv_title.setText(infos.get(position).getTitle());

            return view;
        }

        //Item单击监听事件
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(HistoryActivity.this, ContentActivity.class);
            String idstr = infos.get(position).getPath().substring(
                    infos.get(position).getPath().lastIndexOf("=")+1);
            int show_id = Integer.parseInt(idstr);
            intent.putExtra("show_id", show_id);
            startActivity(intent);
            HistoryActivity.this.finish();//销毁自已
        }

        //Item长按监听事件
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            PopupMenu menu = new PopupMenu(getApplicationContext(),view);
            menu.inflate(R.menu.collect_menu);
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.list_delete) {
                        //删除选中的
                        db = DBUtils.getDBInstance(getApplicationContext());
                        int num = db.delete(DBUtils.TABLE_HISTORY, "path = ?",
                                new String[]{infos.get(position).getPath()});
                        if (num > 0) {
                            Toast.makeText(HistoryActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            //更新数据库
                            infos.clear();
                            getDataFromDB();
                            adapter.notifyDataSetChanged();
                        }
                    }
                    return true;
                }
            });

            menu.show();

            return false;
        }

        class ViewHolder {
            private TextView tv_path ;
            private TextView tv_title ;
        }
    }
}
