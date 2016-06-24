package com.liang.day29_project.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liang.day29_project.R;
import com.liang.day29_project.beans.Info;
import com.liang.day29_project.utils.Constant;
import com.liang.httplibs.BitmapRequest;
import com.liang.httplibs.HttpHelper;
import com.liang.httplibs.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class C_InfoListAdapter extends BaseAdapter{
    private List<Info> infos ;
    private String localPath = null ;
    public C_InfoListAdapter(List<Info> infos){
        this.infos = infos;
        localPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
                +"healthy";
    }
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Info getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ;
        if(convertView == null){
           convertView = View.inflate(parent.getContext(),R.layout.item_listview,null);
            holder = new ViewHolder();
            holder.decs = (TextView) convertView.findViewById(R.id.item_desc);
            holder.rcount = (TextView) convertView.findViewById(R.id.item_count);
            holder.time = (TextView) convertView.findViewById(R.id.item_time);
            holder.img = (ImageView) convertView.findViewById(R.id.item_iv);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        //设置数据
        holder.decs.setText(infos.get(position).getDesc() + "");
        holder.rcount.setText("阅读:"+infos.get(position).getRcount() + "次");
        holder.img.setImageResource(R.mipmap.ic_launcher);//设置默认的一张图片
        String time = infos.get(position).getTime();
        //设置时间
        holder.time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(Long.parseLong(time))+ "");



        //设置图片
        String url = Constant.path_img + infos.get(position).getImg();
        String img_name = url.substring(url.lastIndexOf("/") + 1);
        url =  url + "_100x100";

        holder.img.setTag(url);//设置tag
        //1.本地那图片
        if(localPath != null ){
            File image = new File(localPath,img_name);
            if(image.exists()){
                Bitmap bitmap = getBitmapFromSDCard(image);
                if(bitmap != null && ((String)(holder.img.getTag())).equals(url)){
                    holder.img.setImageBitmap(bitmap);
                    return convertView;
                }
            }
        }
            // 2. 开启线程下载图片，网络那图片
            loadImage(holder.img, url,img_name);
        return convertView;
    }

    class ViewHolder{
        private TextView decs ;
        private TextView time ;
        private TextView rcount ;
        private ImageView img ;
    }

    private void loadImage(final ImageView iv ,final String url,final String filenmae){
        BitmapRequest request = new BitmapRequest
                (url, Request.Method.GET, new Request.CallBack<Bitmap>() {
            @Override
            public void onError(Exception e) {

            }
            @Override
            public void onResponse(final Bitmap result) {
                if(result != null && ((String)(iv.getTag())).equals(url)){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(result);
                            saveBitmap2SDCard(result,filenmae);
                        }
                    });
                }
            }
        });

        HttpHelper.addRequest(request);
    }

    //向sdcard保存图片
    private boolean saveBitmap2SDCard(Bitmap b,String filename){
        if(localPath == null) return false;

        File dir = new File(localPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(dir.getAbsolutePath(),filename);

        try {
            OutputStream out = new FileOutputStream(file);

            b.compress(Bitmap.CompressFormat.PNG,100,out);
            out.close();
            return true ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false ;
    }

    //向sdcard拿图片

    private Bitmap getBitmapFromSDCard(File file){
        Bitmap bitmap = null ;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  bitmap ;
    }
}
