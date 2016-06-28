package com.lettalk.gy.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @author :smile
 * @project:DisplayConfig
 * @date :2016-01-25-09:19
 * 注：由于Picasso圆角处理不够完美，故舍弃
 */
public class DisplayConfigUtils {

    /**
     * UIL默认的显示配置:圆角 12度
     *
     * @param defaultRes
     * @return
     */
    public static DisplayImageOptions getDefaultOptions(boolean hasRounded, int defaultRes) {
       /* DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型:设置为RGB565比起默认的ARGB_8888要节省大量的内存
//                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .resetViewBeforeLoading(true);//设置图片在下载前是否重置，复位
        if (hasRounded) {
            builder.displayer(new RoundedBitmapDisplayer(12));//是否设置为圆角，弧度为多少
        }
        if (defaultRes != 0) {
            builder.showImageForEmptyUri(defaultRes)//设置图片Uri为空或是错误的时候显示的图片
//                            .showImageOnLoading(defaultRes) //设置图片在下载期间显示的图片-->应该去掉-会造成ListView中图片闪烁
                    .showImageOnFail(defaultRes);  //设置图片加载/解码过程中错误时候显示的图片
        }
        return builder.build();//构建完成*/
        return getDefaultOptions(hasRounded, 12, defaultRes, 0);
    }

    /**
     * 没有圆角
     *
     * @param defaultRes
     * @return
     */
    public static DisplayImageOptions getDefaultOptions(int defaultRes, int onload) {
        return getDefaultOptions(false, 0, defaultRes, onload);
    }


    /**
     * UIL默认的显示配置:可设置圆角度
     *
     * @param defaultRes
     * @return
     */
    public static DisplayImageOptions getDefaultOptions(boolean hasRounded, int RoundValue, int defaultRes, int load) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型:设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .resetViewBeforeLoading(true);//设置图片在下载前是否重置，复位

        if (load != 0) {
            builder.showImageOnLoading(load);
        }
        if (hasRounded) {

            builder.displayer(new RoundedBitmapDisplayer(RoundValue));//是否设置为圆角，弧度为多少
        }
        if (defaultRes != 0) {
            builder.showImageForEmptyUri(defaultRes)//设置图片Uri为空或是错误的时候显示的图片
//                            .showImageOnLoading(defaultRes) //设置图片在下载期间显示的图片-->应该去掉-会造成ListView中图片闪烁
                    .showImageOnFail(defaultRes);  //设置图片加载/解码过程中错误时候显示的图片
        }
        return builder.build();//构建完成
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
