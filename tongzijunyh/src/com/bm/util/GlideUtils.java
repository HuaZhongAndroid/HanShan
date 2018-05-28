package com.bm.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * Created by zh on 2018/5/25.
 */

public class GlideUtils {
    static {


    }

    /**
     * 圆角图片
     * @param context
     * @param url
     * @param imageView
     * @param round
     */
    public static void loadRoundImg(Context context, String url, ImageView imageView,int round){
        Glide.with(context)
                .load(url)
                .transform(new CenterCrop(context),new GlideRoundTransform(context,round))
                .into(imageView);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRoundImg(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .transform(new CenterCrop(context),new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 加载图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImg(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * 加载图片
     * @param context
     * @param url
     * @param imageView
     * @param r 占位图
     */
    public static void loadImg(Context context, String url, ImageView imageView,int r){
        Glide.with(context)
                .load(url)
                .placeholder(r)
                .error(r)
                .into(imageView);
    }

}


