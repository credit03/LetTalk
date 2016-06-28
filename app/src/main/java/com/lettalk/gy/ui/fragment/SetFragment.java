package com.lettalk.gy.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.base.ParentWithNaviFragment;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.model.UserModel;
import com.lettalk.gy.ui.LoginActivity;
import com.lettalk.gy.ui.UserInfoAcivityPro;

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
public class SetFragment extends ParentWithNaviFragment {

    @Bind(R.id.tv_set_name)
    TextView tv_set_name;

    @Bind(R.id.layout_info)
    RelativeLayout layout_info;

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
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        upDateNickName();
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
        UserModel.getInstance().logout();
        //可断开连接
        BmobIM.getInstance().disConnect();
        getActivity().finish();
        startActivity(LoginActivity.class, null);
    }
}
