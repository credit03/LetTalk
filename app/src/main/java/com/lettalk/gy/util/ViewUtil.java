package com.lettalk.gy.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author :smile
 * @project:ViewUtil
 * @date :2016-01-25-9:57
 */
public class ViewUtil {


    public static void setAvatar(String url, int defaultRes, ImageView v) {
        setImageView(url, defaultRes, v);
    }

    public static void setImageView(String url, int defaultRes, int onlaod, ImageView v, int RoudValue) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.equals(v.getTag())) {//增加tag标记，减少UIL的display次数
                v.setTag(url);
                //不直接display imageview改为ImageAware，解决ListView滚动时重复加载图片
                ImageAware imageAware = new ImageViewAware(v, false);
                ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfigUtils.getDefaultOptions(true, RoudValue, defaultRes, 0));
            }
        } else {
            v.setImageResource(defaultRes);
        }
    }

    /**
     * 设置圆形图片
     *
     * @param url
     * @param defaultRes
     * @param v
     */
    public static void setImageView(String url, int defaultRes, ImageView v, int RoudValue) {
        setImageView(url, defaultRes, 0, v, RoudValue);
    }

    /**
     * 设置背景（String类型）
     *
     * @param url
     * @param defaultRes
     * @param v
     */
    public static void setImageView(String url, int defaultRes, ImageView v, boolean isRoud) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.equals(v.getTag())) {//增加tag标记，减少UIL的display次数
                v.setTag(url);
                //不直接display imageview改为ImageAware，解决ListView滚动时重复加载图片
                ImageAware imageAware = new ImageViewAware(v, false);

                ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfigUtils.getDefaultOptions(isRoud, defaultRes));
            }
        } else {
            v.setImageResource(defaultRes);
        }
    }

    /**
     * 设置背景（String类型）默认true圆角
     *
     * @param url
     * @param defaultRes
     * @param v
     */
    public static void setImageView(String url, int defaultRes, ImageView v) {
        setImageView(url, defaultRes, v, true);
    }


    /**
     * 设置方形图片
     *
     * @param url
     * @param defaultRes
     * @param onload
     * @param v
     */
    public static void setImageView(String url, int defaultRes, int onload, ImageView v) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.equals(v.getTag())) {//增加tag标记，减少UIL的display次数
                v.setTag(url);
                //不直接display imageview改为ImageAware，解决ListView滚动时重复加载图片
                ImageAware imageAware = new ImageViewAware(v, false);
                ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfigUtils.getDefaultOptions(defaultRes, onload));
            }
        } else {
            v.setImageResource(defaultRes);
        }
    }

    /**
     * 显示图片
     *
     * @param url
     * @param defaultRes
     * @param iv
     * @param listener
     */
    public static void setPicture(String url, int defaultRes, ImageView iv, ImageLoadingListener listener) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.equals(iv.getTag())) {//增加tag标记，减少UIL的display次数
                iv.setTag(url);
                //不直接display imageview改为ImageAware，解决ListView滚动时重复加载图片
                ImageAware imageAware = new ImageViewAware(iv, false);
                if (listener != null) {
                    ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfigUtils.getDefaultOptions(false, defaultRes), listener);
                } else {
                    ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfigUtils.getDefaultOptions(false, defaultRes));
                }
            }
        } else {
            iv.setImageResource(defaultRes);
        }
    }
}
