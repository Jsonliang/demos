package com.liang.day29_project.apps;

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

public class CollectActivity extends AppCompatActivity {
    private ListView mList;
    private List<CollectInfo> infos = new ArrayList<>();
    private CollectInfo info = null;
    private CollectAdapter adapter ;
    private Toolbar bar;
    private DBUtils db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collect);
        getDataFromDB();
        initView();
        initToolbar();
    }

    private void getDataFromDB() {
         db = DBUtils.getDBInstance(getApplicationContext());
        Cursor cursor = db.query(DBUtils.TABLE_COLLECTION);

        while (cursor.moveToNext()) {
            info = new CollectInfo();
            int index = cursor.getColumnIndex("path");
            info.setPath(cursor.getString(index));
            index = cursor.getColumnIndex("title");
            info.setTitle(cursor.getString(index));
            infos.add(info);
        }
    }

    private void initToolbar() {
        bar = (Toolbar) findViewById(R.id.collect_toolbar);
        bar.setLogo(R.mipmap.ic_launcher);
        bar.setTitle("Healthy");
    }

    private void initView() {
        mList = (ListView) findViewById(R.id.collect_listview);
        adapter = new CollectAdapter();
        mList.setAdapter(adapter);
        //设置Item的单击事件
        mList.setOnItemClickListener(adapter);
        //长按单击事件
        mList.setOnItemLongClickListener(adapter);
    }

    class CollectAdapter extends BaseAdapter implements AdapterView.OnItemClickListener
            , AdapterView.OnItemLongClickListener {

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
            ViewHolder holder = null;
            if (view == null) {
                view = View.inflate(parent.getContext(), R.layout.item_more, null);
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

        //ItemClick单击监听事件
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(CollectActivity.this, ContentActivity.class);
            String idstr = infos.get(position).getPath().substring(
                    infos.get(position).getPath().lastIndexOf("=")+1);
            int show_id = Integer.parseInt(idstr);
            intent.putExtra("show_id", show_id);
            startActivity(intent);
            CollectActivity.this.finish();//销毁自已

        }

        //ItemLongClick 长按单击事件
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            PopupMenu menu = new PopupMenu(getApplicationContext(), view);
            menu.inflate(R.menu.collect_menu);
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.list_delete) {
                        if (item.getItemId() == R.id.list_delete) {
                            //删除选中的
                            db = DBUtils.getDBInstance(getApplicationContext());
                            int num = db.delete(DBUtils.TABLE_COLLECTION,"path = ?",
                                    new String[]{infos.get(position).getPath()});
                            if(num >0 ){
                                Toast.makeText(CollectActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                //更新数据库
                                infos.clear();
                                getDataFromDB();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                    return true;
                }
            });
            menu.show();
            return false;
        }

        class ViewHolder {
            private TextView tv_path;
            private TextView tv_title;
        }
    }
}
