package com.lettalk.gy.ui.euclidui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleksii Shliama on 1/27/15.
 */
public class EuclidListAdapter extends ArrayAdapter<Map<String, Object>> {

    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION_SHORT = "description_short";
    public static final String KEY_DESCRIPTION_FULL = "description_full";

    public static final int IS_CURRENTUSER = 0X100;
    public static final int NO_CURRENTUSER = 0X200;
    public static final int IS_FRIEND = 0x300;
    private EuclidItemButClickListener butClickListener;


    private final LayoutInflater mInflater;
    private List<Map<String, Object>> mData;

    public EuclidListAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
    }


    public void UpdateData(List<Map<String, Object>> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        mData.addAll(data);
    }

    private int CURRENT_USER = 0;
    private int FRIEND = 0;

    public EuclidListAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data, int currentUser) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
        CURRENT_USER = currentUser;
    }

    public EuclidListAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data, int currentUser, int isfriend) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
        CURRENT_USER = currentUser;
        FRIEND = isfriend;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mViewOverlay = convertView.findViewById(R.id.view_avatar_overlay);
            viewHolder.mListItemAvatar = (ImageView) convertView.findViewById(R.id.image_view_avatar);
            viewHolder.mListItemName = (TextView) convertView.findViewById(R.id.text_view_name);
            viewHolder.mListItemDescription = (TextView) convertView.findViewById(R.id.text_view_description);
            viewHolder.ll_but = (LinearLayout) convertView.findViewById(R.id.ll_but);

            viewHolder.add_friend = (Button) convertView.findViewById(R.id.btn_add_friend);
            viewHolder.chat = (Button) convertView.findViewById(R.id.btn_chat);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        ViewUtil.setImageView((String) mData.get(position).get(KEY_AVATAR), R.drawable.head, R.drawable.load, viewHolder.mListItemAvatar, 360);

        viewHolder.mListItemName.setText(mData.get(position).get(KEY_NAME).toString().toUpperCase());
        viewHolder.mListItemDescription.setText((String) mData.get(position).get(KEY_DESCRIPTION_SHORT));
        viewHolder.mViewOverlay.setBackground(EuclidActivity.sOverlayShape);


        if (CURRENT_USER == NO_CURRENTUSER) {
            viewHolder.ll_but.setVisibility(LinearLayout.VISIBLE);
            if (FRIEND != IS_FRIEND) {
                viewHolder.add_friend.setVisibility(Button.VISIBLE);
                viewHolder.add_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (butClickListener != null) {
                            butClickListener.euclidItemAddFriendOnclick(v);
                        }
                    }
                });
            }

            viewHolder.chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (butClickListener != null) {
                        butClickListener.euclidItemChatOnclick(v);

                    }
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        View mViewOverlay;
        ImageView mListItemAvatar;
        TextView mListItemName;
        TextView mListItemDescription;
        LinearLayout ll_but;
        Button add_friend;
        Button chat;
    }


    public EuclidItemButClickListener getButClickListener() {
        return butClickListener;
    }

    public void setButClickListener(EuclidItemButClickListener butClickListener) {
        this.butClickListener = butClickListener;
    }
}
