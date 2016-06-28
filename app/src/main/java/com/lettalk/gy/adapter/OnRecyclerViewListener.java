package com.lettalk.gy.adapter;

import android.view.View;

/**
 * 为RecycleView添加点击事件
 *
 * @author smile
 * @project OnRecyclerViewListener
 * @date 2016-03-03-16:39
 */
public interface OnRecyclerViewListener {

    int ACTION_UP = 0X100;

    void onItemClick(int position);

    void onItemClick(int position, View view, BaseViewHolder baseViewHolder);

    boolean onItemLongClick(int position);

    boolean onItemLongClick(int position, View view, BaseViewHolder baseViewHolder);


    /**
     * 右滑动
     *
     * @param position
     * @param view
     * @param baseViewHolder
     * @return
     */
    boolean onItemTouchRightClick(int position, View view, BaseViewHolder baseViewHolder);

    /**
     * 左滑动
     *
     * @param position
     * @param view
     * @param baseViewHolder
     * @return
     */
    boolean onItemTouchLeftClick(int position, View view, BaseViewHolder baseViewHolder);

    boolean OnItemTouchMove(int position, View view, BaseViewHolder baseViewHolder);

    boolean OnItemTouchMove(int position);

    void deleteConversation(int position, View view);

    void deleteConversation(int position);


}
