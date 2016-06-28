package com.lettalk.gy.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lettalk.gy.R;
import com.lettalk.gy.base.ParentWithNaviFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-05-31.
 */
public class FindFragment extends ParentWithNaviFragment {
    @Bind(R.id.rc_view)
    RecyclerView rcView;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout swRefresh;

    @Override
    protected String title() {
        return "发现";
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_find, container, false);
        // initNaviView();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
