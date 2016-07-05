package com.lettalk.gy.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lettalk.gy.R;
import com.lettalk.gy.adapter.OnRecyclerImp;
import com.lettalk.gy.adapter.find.FindAdapter;
import com.lettalk.gy.base.ParentWithNaviActivity;
import com.lettalk.gy.base.ParentWithNaviFragment;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.ui.view.MySwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-05-31.
 */
public class FindFragment extends ParentWithNaviFragment {
    @Bind(R.id.rc_view)
    RecyclerView rcView;
    @Bind(R.id.sw_refresh)
    MySwipeRefreshLayout sw_refresh;
    LinearLayoutManager layoutManager;
    private FindAdapter adapter;


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
        /**
         * 使用RecyclerView 注意事项
         * 1.RecyclerView与SwipeRefreshLayout通常是搭配使用
         * 2.Adapter要继承RecyclerView.Adapter,重写相关的方法，ViewHolder继承RecyclerView.ViewHolder
         * 3.RecyclerView要使用LinearLayoutManager才可以生效
         */

        rootView = inflater.inflate(R.layout.fragment_find, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        adapter = new FindAdapter();
        rcView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rcView.setLayoutManager(layoutManager);

        sw_refresh.setSize(SwipeRefreshLayout.DEFAULT);

        //设置刷新时动画的颜色，可以设置4个
        sw_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        sw_refresh.setEnabled(true);
        setListener();
        return rootView;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {

            }
        };
    }

    private void setListener() {

        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                log("我刷新啦。。setOnRefreshListener");
                sw_refresh.setRefreshing(true);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            runOnMain(new Runnable() {
                                @Override
                                public void run() {
                                    log("停止刷新");
                                    sw_refresh.setRefreshing(false);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });


        adapter.setOnRecyclerViewListener(new OnRecyclerImp() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        initData();
    }

    private void initData() {

        log("initData");
        List<User> users = new ArrayList<>();
        users.add(new User());
        adapter.bindDatas(users);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
