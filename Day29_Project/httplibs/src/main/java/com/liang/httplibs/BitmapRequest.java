package com.liang.httplibs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class BitmapRequest extends Request<Bitmap> {

    public BitmapRequest(String url, Method method, CallBack<Bitmap> callBack) {
        super(url, method, callBack);
    }

    @Override
    public void dispatcherContent(byte[] content) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(content,0,content.length);
        onResponse(bitmap);
    }
}
