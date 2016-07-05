package com.lettalk.gy.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lettalk.gy.R;
import com.lettalk.gy.adapter.OnRecyclerImp;
import com.lettalk.gy.adapter.SearchUserAdapter;
import com.lettalk.gy.base.ParentWithNaviActivity;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.model.BaseModel;
import com.lettalk.gy.model.UserModel;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.listener.FindListener;


/**
 * 搜索好友
 *
 * @author :smile
 * @project:SearchUserActivity
 * @date :2016-01-25-18:23
 */
public class SearchUserActivity extends ParentWithNaviActivity {

    @Bind(R.id.et_find_name)
    EditText et_find_name;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @Bind(R.id.btn_search)
    Button btn_search;
    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    LinearLayoutManager layoutManager;
    SearchUserAdapter adapter;

    public static SearchUserActivity getInstance() {
        return instance;
    }

    public static void setInstance(SearchUserActivity instance) {
        SearchUserActivity.instance = instance;
    }

    private static SearchUserActivity instance;


    @Override
    protected String title() {
        return "搜索好友";
    }

    public void finish() {
        setInstance(null);
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initNaviView();
        setInstance(this);
        adapter = new SearchUserAdapter();
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        rc_view.setAdapter(adapter);
        sw_refresh.setEnabled(true);
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });

        adapter.setOnRecyclerViewListener(new OnRecyclerImp() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                User user = adapter.getItem(position);
                /**
                 * 处理显示昵称，不要显示UserName字段，如果是微博授权 UserName字段则是一串16进制数，第一次登录获取微博昵称

                 User u = LetTalkApplication.INSTANCE().getUser(user.getObjectId());
                 if (u != null) {
                 user.setUsername(u.getNickname());
                 } else {

                 log("处理显示昵称 微博授权。。。。");
                 Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(SearchUserActivity.this);
                 UsersAPI usersAPI = new UsersAPI(SearchUserActivity.this, Constants.APP_KEY, oauth2AccessToken);
                 usersAPI.show(user.getUsername(), new RequestListener() {
                @Override public void onComplete(String s) {
                log("返回结果：" + s);
                }

                @Override public void onWeiboException(WeiboException e) {
                log("返回结果：" + e.getMessage());
                }
                });
                 }
                 */

                bundle.putSerializable("u", user);
                // startActivity(UserInfoActivity.class, bundle, false);
                startActivity(UserInfoAcivityPro.class, bundle, false);
            }


            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }


        });
    }

    @OnClick(R.id.btn_search)
    public void onSearchClick(View view) {
        sw_refresh.setRefreshing(true);
        query();
    }

    public void query() {
        String name = et_find_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toast("请填写用户名");
            sw_refresh.setRefreshing(false);
            return;
        }

        UserModel.getInstance().queryUsersByOr(name, BaseModel.DEFAULT_LIMIT, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                sw_refresh.setRefreshing(false);
                adapter.setDatas(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                sw_refresh.setRefreshing(false);
                adapter.setDatas(null);
                adapter.notifyDataSetChanged();
                toast(s + "(" + i + ")");
            }
        });
    }


}
