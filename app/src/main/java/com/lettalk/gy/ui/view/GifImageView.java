package com.lettalk.gy.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.lettalk.gy.R;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016-06-24.
 */
public class GifImageView extends ImageView implements View.OnClickListener {

    private Movie mMovie;   //播放GIF动画的关键类

    private Bitmap mStartBitmap;   //播放按钮

    private long mMovieStart;  //动画开始的时间
    private int mImageWidth;  //GIF图片的宽度

    private int mImageHeight;  //GIF图片的高度

    private boolean isPlaying;   //标志是否正在播放

    private boolean isAutoPlay;  //是否自动播放



    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PowerImageView);
        int resourceId = getResourceId(a, context, attrs);
        if (resourceId != 0) {
            //获取该资源的流
            InputStream is = getResources().openRawResource(resourceId);
            //使用Movie对流进行解码
            mMovie = Movie.decodeStream(is);
            if (mMovie != null) {
                //如果返回不为空,则说明这是一个GIF图片,下面获取自动播放的属性
                isAutoPlay = a.getBoolean(R.styleable.PowerImageView_auto_play, false);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                mImageWidth = bitmap.getWidth() ;
                mImageHeight = bitmap.getHeight() ;

                bitmap.recycle();

                if (!isAutoPlay) {
                    setListener();
                    mStartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.load2);

                }
            }
        }

    }

    /**
     * 防止多次设置事件
     */
    private boolean Cilckset = false;

    public void setListener() {
        if (!Cilckset) {
            Cilckset = true;
            setOnClickListener(this);
        }
    }


    //通过Java反射，获取到src指定图片资源所对应的id。
    private int getResourceId(TypedArray a, Context context, AttributeSet attrs) {
        try {
            Field field = TypedArray.class.getDeclaredField("mValue");
            field.setAccessible(true);
            TypedValue typeValueObject = (TypedValue) field.get(a);
            return typeValueObject.resourceId;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == getId()) {
            isPlaying = true;
            invalidate();
        } else if (!Cilckset) {
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie == null) {
            super.onDraw(canvas);
        } else {
            if (isAutoPlay) {
                playMovie(canvas);
                invalidate();
            } else {
                if (isPlaying) {
                    if (playMovie(canvas)) {
                        isPlaying = false;
                    }
                    invalidate();
                } else {
                    mMovie.setTime(0);
                    mMovie.draw(canvas, 0, 0);
                    int offsetW = (mImageWidth - mStartBitmap.getWidth()) / 2;
                    int offsetH = (mImageHeight = mStartBitmap.getHeight()) / 2;
                    canvas.drawBitmap(mStartBitmap, offsetW, offsetH, null);
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMovie != null) {
            setMeasuredDimension(mImageWidth, mImageHeight);
        }
    }

    //开始播放GIF动画   当播放结束返回true.
    private boolean playMovie(Canvas canvas) {
        long now = SystemClock.uptimeMillis();   //获取从手机开机到现在的毫秒数
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        int duration = mMovie.duration();
        if (duration == 0) {
            duration = 1000;
        }
        int relTime = (int) ((now - mMovieStart) % duration);
        mMovie.setTime(relTime);
        mMovie.draw(canvas, 0, 0);
        if ((now - mMovieStart) >= duration) {
            mMovieStart = 0;
            return true;
        }
        return false;
    }
}
