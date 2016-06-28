package com.lettalk.gy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.bean.Friend;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.util.ViewUtil;

import butterknife.Bind;

public class ContactHolder extends BaseViewHolder {

    @Bind(R.id.iv_recent_avatar)
    public ImageView iv_recent_avatar;
    @Bind(R.id.tv_recent_name)
    public TextView tv_recent_name;
    @Bind(R.id.delete_friend)
    public TextView deleteFriend;

    public ContactHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_contact, onRecyclerViewListener);
        setListener();
    }

    @Override
    public void bindData(Object o) {
        Friend friend = (Friend) o;
        User user = friend.getFriendUser();
        //会话图标
        ViewUtil.setAvatar(user == null ? null : user.getAvatar(), R.mipmap.head, iv_recent_avatar);
        //会话标题
        tv_recent_name.setText(user == null ? "未知" : user.getNickname());
    }

    private boolean is_touch = false;

    public boolean is_touch() {
        return is_touch;
    }

    public void setIs_touch(boolean is_touch) {
        this.is_touch = is_touch;
    }

    private void setListener() {
        deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.deleteConversation(getAdapterPosition());
                    onRecyclerViewListener.deleteConversation(getAdapterPosition(), v);
                }
            }
        });
    }

}