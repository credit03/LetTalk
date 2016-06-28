package com.lettalk.gy.adapter;

import android.view.View;

/**
 * Created by Administrator on 2016-06-08.
 */
public abstract class OnRecyclerImp implements OnRecyclerViewListener {
    @Override
    public void onItemClick(int position, View view, BaseViewHolder baseViewHolder) {

    }

    @Override
    public boolean onItemLongClick(int position, View view, BaseViewHolder baseViewHolder) {
        return false;
    }

    @Override
    public boolean onItemTouchRightClick(int position, View view, BaseViewHolder baseViewHolder) {
        return false;
    }

    @Override
    public boolean onItemTouchLeftClick(int position, View view, BaseViewHolder baseViewHolder) {
        return false;
    }

    @Override
    public void deleteConversation(int position, View view) {

    }

    @Override
    public void deleteConversation(int position) {

    }

    @Override
    public boolean OnItemTouchMove(int position, View view, BaseViewHolder baseViewHolder) {
        return false;
    }

    @Override
    public boolean OnItemTouchMove(int position) {
        return false;
    }
}
