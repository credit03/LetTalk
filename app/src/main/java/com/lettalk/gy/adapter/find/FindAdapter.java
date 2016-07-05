package com.lettalk.gy.adapter.find;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.lettalk.gy.adapter.BaseViewHolder;
import com.lettalk.gy.adapter.OnRecyclerViewListener;
import com.lettalk.gy.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-02.
 */
public class FindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final String TAG = "FindAdapter";
    //文本信息
    private final int TYPE_TEXT_CONTENT = 0;
    //图片
    private final int TYPE_IMAGE = 1;
    //单张图片和内容
    private final int TYPE_TEXTIMAGE_CONTENT = 2;
    //多张图片和内容
    private final int TYPE_TEXT_MOREIMAGE_CONTENT = 3;


    //gif图像
    private final int TYPE_GIFIAMGE = 4;
    //单张gif图像和内容
    private final int TYPE_TEXT_GIF = 5;

    private List<User> users = new ArrayList<User>();

    public FindAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: 方法");
        return new FindImageHolder(parent.getContext(), parent, onRecyclerViewListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FindImageHolder) {
            Log.i(TAG, "onBindViewHolder: 绑定view");
            ((BaseViewHolder) holder).bindData(getItem(position));
        } else {
            Log.i(TAG, "onBindViewHolder: 没有绑定view");
        }


    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_TEXT_CONTENT;
        }
        return TYPE_GIFIAMGE;


    }

    /**
     * @param list
     */
    public void bindDatas(List<User> list) {
        users.clear();
        if (null != list) {
            users.addAll(list);
        }
    }


    /**
     * 移除会话
     *
     * @param position
     */
    public void remove(int position) {
        users.remove(position);
        notifyDataSetChanged();
    }

    public User getItem(int position) {

        Log.i(TAG, "getItem: 获取User" + position);
        return users.get(position);
    }


    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    @Override
    public int getItemCount() {

        Log.i(TAG, "getItemCount: 获取总数" + users.size());
        return users.size();
    }
}
