package com.lettalk.gy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lettalk.gy.R;
import com.lettalk.gy.db.NewFriendManager;

import butterknife.Bind;

/**
 * 新朋友按钮
 */
public class ContactNewFriendHolder extends BaseViewHolder {

  @Bind(R.id.iv_msg_tips)
  public ImageView iv_msg_tips;

  public ContactNewFriendHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.header_new_friend,onRecyclerViewListener);
  }

  @Override
  public void bindData(Object o) {
    //是否有好友添加的请求
    if(NewFriendManager.getInstance(getContext()).hasNewFriendInvitation()){
        iv_msg_tips.setVisibility(View.VISIBLE);
    }else{
        iv_msg_tips.setVisibility(View.GONE);
    }
  }

}