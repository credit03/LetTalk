package com.lettalk.gy.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Administrator on 2016-06-13.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    private int mTouchSlop;
    // 上一次触摸时的Y坐标
    private float mPrevY;

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 触发移动事件的最短距离，如果小于这个距离就不触发移动控件getScaledTouchSlop();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                final float eventY = event.getY();
                float xDiff = Math.abs(eventY - mPrevY);
                // Log.d("refresh" ,"move----" + eventX + "   " + mPrevY + "   " + mTouchSlop);
                // 增加60的容差，让下拉刷新在竖直滑动时就可以触发
                if (xDiff < mTouchSlop + 500) {
                    return false;
                }

                break;
        }

        return super.onInterceptTouchEvent(event);
    }
}