package com.lettalk.gy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lettalk.gy.base.BaseActivity;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.model.UserModel;
import com.lettalk.gy.model.i.QueryUserListener;
import com.lettalk.gy.ui.UserInfoAcivityPro;

import butterknife.ButterKnife;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.v3.exception.BmobException;


public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {

    protected static final String TAG = "BaseViewHolder";
    OnRecyclerViewListener onRecyclerViewListener;
    protected Context context;

    private boolean can_copy = false;

    public BaseViewHolder(Context context, ViewGroup root, int layoutRes, OnRecyclerViewListener listener) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
        this.context = context;
        ButterKnife.bind(this, itemView);
        this.onRecyclerViewListener = listener;
        // itemView.setOnTouchListener(this);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public abstract void bindData(T t);

    private Toast toast;

    public void toast(final Object obj) {
        try {
            ((BaseActivity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewListener != null) {
            onRecyclerViewListener.onItemClick(getAdapterPosition(), v, this);
            onRecyclerViewListener.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onRecyclerViewListener != null) {
            onRecyclerViewListener.onItemLongClick(getAdapterPosition());
            Log.i(TAG, "onLongClick: onItemLongClick(getAdapterPosition())>>>>>>下一步");
            onRecyclerViewListener.onItemLongClick(getAdapterPosition(), v, this);
            Log.i(TAG, "onLongClick: 完成。。下一步");
        }
        return true;
    }

    /**
     * 启动指定Activity
     *
     * @param target
     * @param bundle
     */
    public void startActivity(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getContext(), target);
        if (bundle != null)
            intent.putExtra(getContext().getPackageName(), bundle);
        getContext().startActivity(intent);
    }

    public void QueryUserInfo(BmobIMConversation c) {

        if (c == null) {
            return;
        }
        UserModel.getInstance().queryUserInfo(c.getConversationId().toString(), new QueryUserListener() {
            @Override
            public void done(User s, BmobException e) {
                Bundle bundle = new Bundle();
                Log.i(TAG, "done: 用户ID==" + s.getObjectId());
                bundle.putSerializable("u", s);
                startActivity(UserInfoAcivityPro.class, bundle);
            }
        });


    }

    private boolean isTouch = false;
    private float start_x = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        isTouch = false;
        // TODO Auto-generated method stub
        /**
         * ACTION_MASK  action码的位掩码部分就是action本身，相与就得到动作常量
         */
        switch (event.getAction()) {
            //int是32位数据，屏幕触摸事件是低8位，相与取其低8位   例：0XFFFF 取其低8位 0xFFFF&0X000F=0XF;

            case MotionEvent.ACTION_DOWN://当手指按下时
                isTouch = false;
                start_x = event.getX();
                break;


            case MotionEvent.ACTION_UP://当手指抬起离开时（释放）
                final float x = event.getX();
                if (Math.abs(x - start_x) >= 25) {
                    if (x > start_x) {
                        if (onRecyclerViewListener != null) {
                            isTouch = onRecyclerViewListener.onItemTouchRightClick(getAdapterPosition(), v, this);
                        }
                    } else {
                        if (onRecyclerViewListener != null) {
                            isTouch = onRecyclerViewListener.onItemTouchLeftClick(getAdapterPosition(), v, this);
                        }
                    }
                    break;
                }
        }
        return isTouch;
    }

    public boolean isCan_copy() {
        return can_copy;
    }

    public void setCan_copy(boolean can_copy) {
        this.can_copy = can_copy;
    }

}