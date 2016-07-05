package com.lettalk.gy.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.base.ParentWithNaviActivity;
import com.lettalk.gy.base.ParentWithNaviFragment;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.model.UserModel;
import com.lettalk.gy.ui.AboutActivity;
import com.lettalk.gy.ui.LoginActivity;
import com.lettalk.gy.ui.UserInfoAcivityPro;
import com.lettalk.gy.util.sina.AccessTokenKeeper;
import com.lettalk.gy.util.sina.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;

/**
 * 设置
 *
 * @author :smile
 * @project:SetFragment
 * @date :2016-01-25-18:23
 */
public class SetFragment extends ParentWithNaviFragment implements ParentWithNaviActivity.ToolBarListener {

    @Bind(R.id.tv_set_name)
    TextView tv_set_name;

    @Bind(R.id.layout_info)
    RelativeLayout layout_info;
    @Bind(R.id.ll_all)
    LinearLayout ll_all;
    int mScreenWidth;

    @Override
    protected String title() {
        return "设置";
    }

    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_set, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        upDateNickName();


    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return this;
    }

    @Override
    public Object right() {
        return "更多...";
    }

    /**
     * 更新昵称
     */
    public void upDateNickName() {
        String usernickname = UserModel.getInstance().getCurrentUser().getNickname();
        tv_set_name.setText(TextUtils.isEmpty(usernickname) ? "未命名" : usernickname);
    }


    @OnClick(R.id.layout_info)
    public void onInfoClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("u", BmobUser.getCurrentUser(getActivity(), User.class));
        startActivity(UserInfoAcivityPro.class, bundle);
    }

    @OnClick(R.id.btn_logout)
    public void onLogoutClick(View view) {

        Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        if (oauth2AccessToken.isSessionValid()) {
            LogoutAPI api = new LogoutAPI(getActivity(), Constants.APP_KEY, oauth2AccessToken);
            api.logout(new RequestListener() {
                @Override
                public void onComplete(String s) {
                    AccessTokenKeeper.clear(getActivity());
                }

                @Override
                public void onWeiboException(WeiboException e) {

                }
            });
        }

        UserModel.getInstance().logout();
        //可断开连接
        BmobIM.getInstance().disConnect();
        getActivity().finish();
        startActivity(LoginActivity.class, null);
    }

    @Override
    public void clickLeft() {

    }

    @Override
    public void clickRight() {

        showAvatarPop();
    }

    RelativeLayout layout_about;
    RelativeLayout layout_logout;
    PopupWindow mroePop;


    @SuppressWarnings("deprecation")
    private void showAvatarPop() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_showlogout,
                null);
        layout_about = (RelativeLayout) view.findViewById(R.id.layout_about);
        layout_logout = (RelativeLayout) view.findViewById(R.id.layout_logout);
        layout_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                layout_about.setBackgroundColor(getResources().getColor(
                        R.color.base_color_text_white));
                layout_logout.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                mroePop.dismiss();
                onLogoutClick(v);
            }
        });
        layout_about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layout_logout.setBackgroundColor(getResources().getColor(
                        android.R.color.holo_red_dark));
                layout_about.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                mroePop.dismiss();

                startActivity(AboutActivity.class, null);
            }
        });


        mroePop = new PopupWindow(view, mScreenWidth, 300);
        mroePop.setFocusable(true);
        mroePop.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        mroePop.showAtLocation(ll_all, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
