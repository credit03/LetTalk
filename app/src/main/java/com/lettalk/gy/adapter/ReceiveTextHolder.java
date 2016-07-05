package com.lettalk.gy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.util.ViewUtil;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 接收到的文本类型
 */
public class ReceiveTextHolder extends BaseViewHolder {

    @Bind(R.id.iv_avatar)
    protected ImageView iv_avatar;

    @Bind(R.id.tv_time)
    protected TextView tv_time;

    @Bind(R.id.tv_message)
    protected TextView tv_message;
    BmobIMConversation c;

    public ReceiveTextHolder(Context context, ViewGroup root, BmobIMConversation c, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_message, onRecyclerViewListener);
        this.c = c;
        setCan_copy(true);
    }

    @OnClick({R.id.iv_avatar})
    public void onAvatarClick(View view) {
        QueryUserInfo(c);
    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        tv_time.setText(time);
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        ViewUtil.setAvatar(info != null ? info.getAvatar() : null, R.mipmap.head, iv_avatar);
        String content = message.getContent();
        tv_message.setText(content);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryUserInfo(c);
            }
        });

        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + message.getContent());
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        tv_message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition(), v, ReceiveTextHolder.this);
                }
                return true;
            }
        });
    }

    public void showTime(boolean isShow) {
        tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public String getMsgText() {
        return tv_message.getText().toString();
    }
}