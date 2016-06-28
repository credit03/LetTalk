package com.lettalk.gy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.db.NewFriendManager;
import com.lettalk.gy.util.ViewUtil;

import butterknife.Bind;

/**
 * Created by Administrator on 2016-05-31.
 */
public class ContactsItemHolder extends BaseViewHolder {

    @Bind(R.id.iv_msg_tips)
    public ImageView iv_msg_tips;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.title)
    TextView title;

    public ContactsItemHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.herader_contacts, listener);

    }

    @Override
    public void bindData(Object o) {
        //是否有好友添加的请求
        if (NewFriendManager.getInstance(getContext()).hasNewFriendInvitation()) {
            iv_msg_tips.setVisibility(View.VISIBLE);
        } else {
            iv_msg_tips.setVisibility(View.GONE);
        }

    }


    public void setIcon(String url) {
        ViewUtil.setImageView(url, R.mipmap.new_friends_icon, icon);
    }


    public void setTitle(String title) {
        this.title.setText(title + " 联系人");
    }
}
