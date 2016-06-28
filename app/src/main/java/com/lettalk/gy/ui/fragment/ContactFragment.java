package com.lettalk.gy.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.adapter.BaseViewHolder;
import com.lettalk.gy.adapter.ContactAdapter;
import com.lettalk.gy.adapter.ContactHolder;
import com.lettalk.gy.adapter.OnRecyclerImp;
import com.lettalk.gy.base.ParentWithNaviActivity.ToolBarListener;
import com.lettalk.gy.base.ParentWithNaviFragment;
import com.lettalk.gy.bean.Friend;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.config.LetTalkApplication;
import com.lettalk.gy.event.RefreshEvent;
import com.lettalk.gy.manage.InterObservable;
import com.lettalk.gy.model.UserModel;
import com.lettalk.gy.ui.ChatActivity;
import com.lettalk.gy.ui.MainActivity;
import com.lettalk.gy.ui.NewFriendActivity;
import com.lettalk.gy.ui.SearchUserActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * 联系人界面
 *
 * @author :smile
 * @project:ContactFragment
 * @date :2016-04-27-14:23
 */
public class ContactFragment extends ParentWithNaviFragment {

    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    ContactAdapter adapter;
    LinearLayoutManager layoutManager;


    private InterObservable interObservable = null;

    public InterObservable getInterObservable() {
        return interObservable;
    }

    public void setInterObservable(InterObservable interObservable) {
        this.interObservable = interObservable;
    }

    @Override
    protected String title() {
        return "联系人";
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public Object left() {
        return R.drawable.base_action_bar_back_bg_selector;
    }

    @Override
    public ToolBarListener setToolBarListener() {
        return new ToolBarListener() {
            @Override
            public void clickLeft() {
                clearItemSelect();
                GoActivitySwitch(BACK_UP);
            }

            @Override
            public void clickRight() {

                if (setRi) {
                    clearItemSelect();
                } else {
                    startActivity(SearchUserActivity.class, null);
                }

            }
        };
    }

    @Override
    public void clearItemSelect() {
        List<ContactHolder> l = new ArrayList(itemHolderLong.values());
        for (ContactHolder co : l) {
            co.setIs_touch(false);
            co.deleteFriend.setVisibility(TextView.GONE);
        }
        itemHolderLong.clear();
        rebackRight();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        adapter = new ContactAdapter();
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setSize(SwipeRefreshLayout.DEFAULT);

        //设置刷新时动画的颜色，可以设置4个
        sw_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        sw_refresh.setEnabled(true);
        setListener();
        return rootView;
    }


    /**
     * 保存用户长按Item的操作
     */
    private HashMap<Integer, ContactHolder> itemHolderLong = new HashMap<Integer, ContactHolder>();

    private void setListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                query();
            }
        });
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerImp() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {//跳转到新朋友页面
                    startActivity(NewFriendActivity.class, null);
                } else {
                    Friend friend = adapter.getItem(position);
                    User user = friend.getFriendUser();
                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                    //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(ChatActivity.class, bundle);

                    adapter.notifyDataSetChanged();
                }
            }


            @Override
            public boolean onItemLongClick(int position, View view, BaseViewHolder baseViewHolder) {
                if (baseViewHolder != null) {
                    ContactHolder holder = (ContactHolder) baseViewHolder;
                    /**
                     * 防止多次触发
                     */
                    if (holder != null && !holder.is_touch()) {
                        onItemTouchLeftClick(position, view, baseViewHolder);
                    } else {
                        onItemTouchRightClick(position, view, baseViewHolder);
                    }

                }
                return false;
            }

            @Override
            public boolean onItemLongClick(final int position) {

                return false;
            }

            @Override
            public boolean onItemTouchLeftClick(int position, View view, BaseViewHolder
                    baseViewHolder) {

                if (baseViewHolder != null) {
                    ContactHolder holder = (ContactHolder) baseViewHolder;
                    /**
                     * 防止多次触发
                     */
                    if (holder != null && !holder.is_touch()) {
                        holder.setIs_touch(true);
                        holder.deleteFriend.setVisibility(TextView.VISIBLE);
                        itemHolderLong.put(position, holder);
                        if (itemHolderLong.size() >= 1 && !setRi) {
                            setRight("取消选择");
                        }
                    }


                }

                return true;
            }

            @Override
            public boolean onItemTouchRightClick(int position, View view, BaseViewHolder
                    baseViewHolder) {
                log("右滑动onItemTouchRightClick");
                if (baseViewHolder != null) {
                    ContactHolder holder = (ContactHolder) baseViewHolder;
                    /**
                     * 防止多次触发
                     */
                    if (holder != null && holder.is_touch()) {
                        holder.setIs_touch(false);
                        holder.deleteFriend.setVisibility(TextView.GONE);
                        if (itemHolderLong.size() == 0 && setRi) {
                            rebackRight();
                        }

                    }
                }
                return true;
            }

            @Override
            public void deleteConversation(final int position, View view) {
                UserModel.getInstance().deleteFriend(adapter.getItem(position), new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        toast("好友删除成功");
                        adapter.remove(position);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        toast("好友删除失败：" + i + ",s =" + s);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        log("ContactFragment  -》》》onResume");
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        //重新刷新列表
        log("---接收到自定义消息---");
        adapter.notifyDataSetChanged();
    }

    /**
     * 查询本地会话
     */
    public void query() {
        UserModel.getInstance().queryFriends(new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                upDateApplicationUsers(list);
                adapter.bindDatas(list);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                adapter.bindDatas(null);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
            }
        });
    }

    /**
     * 更新Application好友列表集合
     *
     * @param list
     */
    public void upDateApplicationUsers(List<Friend> list) {
        log("更新Application好友列表集合");
        LetTalkApplication instance = LetTalkApplication.INSTANCE();
        instance.upDateUserListInfo(list);
        if (interObservable != null) {
            log(" interObservable.ChangeOtherManagerUpdate();好友列表集合");
            interObservable.ChangeOtherManagerUpdate();
        }
    }

    private static final int BACK_UP = 0X100;

    /**
     * @param code =0x100 返回上一个页面，其他自定
     */
    public void GoActivitySwitch(int code) {

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            switch (code) {
                case BACK_UP:
                    activity.back_upPage(1);
                    break;

                default:
                    break;
            }
        }

    }


}
