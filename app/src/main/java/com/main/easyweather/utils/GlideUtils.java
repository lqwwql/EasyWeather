package com.main.easyweather.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.main.easyweather.R;

/**
 * Created by admin on 2018/5/9.
 */

public class GlideUtils {

    public static GlideUtils glideUtils;

    public static GlideUtils getInstence(){
        if(glideUtils==null){
            glideUtils = new GlideUtils();
        }
        return glideUtils;
    }

    /**
     * 禁用缓存
     * 用glide加载网络图片
     * */
    public void setImageResource(String url, Context context, ImageView imageView){
        Glide.with(context)
                .load(url)//可以是网络url，本地图片，应用资源，二进制流，uri对象等
                //.asGif()//制定加载动态图片，非动态则显示error
                //.asBitmap()//指定加载静态图片，gif图只显示第一帧
                .override(80,110)//指定加载图片的大小，不管imageview的大小
                .placeholder(R.drawable.loading)//添加占位图，加载网络图片前显示，加载后替换占位图
                .error(R.drawable.error)//添加加载失败占位图
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用缓存功能
                .into(imageView);//要显示的imageview对象
    }

    /**
     * 使用缓存
     * 用glide加载网络图片
     * */
    public void setImageCacheResource(String url, Context context, ImageView imageView){
        Glide.with(context)
                .load(url)//可以是网络url，本地图片，应用资源，二进制流，uri对象等
                //.asGif()//制定加载动态图片，非动态则显示error
                //.asBitmap()//指定加载静态图片，gif图只显示第一帧
                .override(80,110)//指定加载图片的大小，不管imageview的大小
                .placeholder(R.drawable.loading)//添加占位图，加载网络图片前显示，加载后替换占位图
                .error(R.drawable.error)//添加加载失败占位图
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//使用缓存原始图片功能
                .into(imageView);//要显示的imageview对象
    }

    /**
     * 使用缓存
     * 用glide加载网络图片
     * */
    public void setImageCacheResource(int drawable, Context context, ImageView imageView){
        Glide.with(context)
                .load(drawable)//可以是网络url，本地图片，应用资源，二进制流，uri对象等
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//使用缓存原始图片功能
                .into(imageView);//要显示的imageview对象
    }

    /**
     * 禁用缓存
     * 用glide加载本地图片
     * */
    public void setLocalImageResource(String localUrl, Context context, ImageView imageView){
        Glide.with(context)
                .load(localUrl)//可以是网络url，本地图片，应用资源，二进制流，uri对象等
                //.asGif()//制定加载动态图片，非动态则显示error
                //.asBitmap()//指定加载静态图片，gif图只显示第一帧
                .override(80,110)//指定加载图片的大小，不管imageview的大小
//                .placeholder(R.drawable.loading)//添加占位图，加载网络图片前显示，加载后替换占位图
//                .error(R.drawable.error)//添加加载失败占位图
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用缓存功能
                .into(imageView);//要显示的imageview对象
    }

}
