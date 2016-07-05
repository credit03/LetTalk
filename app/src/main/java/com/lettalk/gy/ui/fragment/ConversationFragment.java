package com.lettalk.gy.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.adapter.BaseViewHolder;
import com.lettalk.gy.adapter.ContactsItemHolder;
import com.lettalk.gy.adapter.ConversationAdapter;
import com.lettalk.gy.adapter.ConversationHolder;
import com.lettalk.gy.adapter.OnRecyclerImp;
import com.lettalk.gy.base.ParentWithNaviActivity.ToolBarListener;
import com.lettalk.gy.base.ParentWithNaviFragment;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.config.LetTalkApplication;
import com.lettalk.gy.manage.InterObservable;
import com.lettalk.gy.ui.ChatActivity;
import com.lettalk.gy.ui.MainActivity;
import com.lettalk.gy.ui.SearchUserActivity;
import com.lettalk.gy.ui.view.MySwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 会话界面
 *
 * @author :smile
 * @project:ConversationFragment
 * @date :2016-01-25-18:23
 */
public class ConversationFragment extends ParentWithNaviFragment implements InterObservable {

    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    @Bind(R.id.sw_refresh)
    MySwipeRefreshLayout sw_refresh;

    ConversationAdapter adapter;
    LinearLayoutManager layoutManager;


    @Override
    protected String title() {
        return "会话";
    }


    @Override
    public Object right() {
        return R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public ToolBarListener setToolBarListener() {
        return new ToolBarListener() {
            @Override
            public void clickLeft() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        adapter = new ConversationAdapter();
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rc_view.setLayoutManager(layoutManager);
        //显示刷新
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
    private HashMap<Integer, ConversationHolder> itemHolderLong = new HashMap<Integer, ConversationHolder>();

    private void setListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                // query();
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

                                              }

                                              @Override
                                              public void onItemClick(int position, View view, BaseViewHolder baseViewHolder) {
                                                  /**
                                                   * 跳到联系人
                                                   */
                                                  if (position == 0) {

                                                      if (getActivity() != null && getActivity() instanceof MainActivity) {
                                                          MainActivity activity = (MainActivity) getActivity();
                                                          activity.goToContacts();
                                                      }
                                                      clearItemSelect();

                                                  } else {


                                                      Bundle bundle = new Bundle();
                                                      BmobIMConversation c = adapter.getItem(position);
                                                      bundle.putSerializable("c", c);
                                                      startActivity(ChatActivity.class, bundle);
                                                      clearItemSelect();
                                                  }
                                              }


                                              @Override
                                              public boolean onItemLongClick(int position) {
                                                  return false;
                                              }

                                              @Override
                                              public boolean onItemLongClick(int position, View view, BaseViewHolder baseViewHolder) {

                                                  if (baseViewHolder != null) {
                                                      ConversationHolder holder = (ConversationHolder) baseViewHolder;
                                                      /**
                                                       * 防止多次触发
                                                       */
                                                      if (holder != null && !holder.is_touch()) {
                                                          onItemTouchLeftClick(position, view, baseViewHolder);
                                                      } else {
                                                          onItemTouchRightClick(position, view, baseViewHolder);
                                                      }

                                                  }

                                                  return true;
                                              }

                                              @Override
                                              public void deleteConversation(int position, View view) {
                                                  log("两种方式均可以删除会话deleteConversation  BY VIEW   " + position);
                                                  //以下两种方式均可以删除会话
//                                              BmobIM.getInstance().deleteConversation(adapter.getItem(position).getConversationId());
                                                  clearItemSelect();
                                                  BmobIM.getInstance().deleteConversation(adapter.getItem(position));
                                                  adapter.remove(position);
                                                  adapter.notifyDataSetChanged();
                                              }

                                              @Override
                                              public boolean onItemTouchLeftClick(int position, View view, BaseViewHolder
                                                      baseViewHolder) {

                                                  if (baseViewHolder != null) {
                                                      ConversationHolder holder = (ConversationHolder) baseViewHolder;
                                                      /**
                                                       * 防止多次触发
                                                       */
                                                      if (holder != null && !holder.is_touch()) {
                                                          holder.setIs_touch(true);
                                                          holder.delete.setVisibility(TextView.VISIBLE);
                                                          itemHolderLong.put(position, holder);
                                                          if (itemHolderLong.size() >= 1 && !setRi) {
                                                              setRight("取消选择");
                                                          }
                                                        /*  map.put(position, baseViewHolder);
                                                          *//**
                                                           * 延时30秒发送
                                                           *//*
                                                          Message msg = Message.obtain();
                                                          msg.arg1 = position;
                                                          msg.what = 100;
                                                          handler.sendMessageDelayed(msg, 150 * 1000);*/
                                                      }


                                                  }

                                                  return true;
                                              }

                                              @Override
                                              public boolean onItemTouchRightClick(int position, View view, BaseViewHolder
                                                      baseViewHolder) {
                                                  log("右滑动onItemTouchRightClick");
                                                  if (baseViewHolder != null) {
                                                      ConversationHolder holder = (ConversationHolder) baseViewHolder;
                                                      /**
                                                       * 防止多次触发
                                                       */
                                                      if (holder != null && holder.is_touch()) {
                                                          holder.setIs_touch(false);
                                                          holder.delete.setVisibility(TextView.GONE);
                                                          itemHolderLong.remove(position);
                                                          if (itemHolderLong.size() == 0 && setRi) {
                                                              rebackRight();
                                                          }
                                                        /*  map.remove(position);*/
                                                      }
                                                  }
                                                  return true;
                                              }
                                          }

        );

    }


    /**
     * 上一次刷新时间
     */
    private long last_time = 0;

    @Override
    public void onResume() {
        super.onResume();
        log("ConversationFragment ===onResume");
        if (System.currentTimeMillis() > (last_time + 5 * 1000 * 60)) {
            sw_refresh.setRefreshing(true);
            last_time = System.currentTimeMillis();
        }
        query();
    }

    @Override
    public void onPause() {
        log("ConversationFragment onPause");
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        log("Conversation方法 onStart");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        log("Conversation方法 onStop");
        super.onStop();
    }

    /**
     * 查询本地会话
     */

    public void query() {
        /**
         * 更新数据
         */
        final ContactsItemHolder itemHolder = adapter.getItemHolder();
        if (itemHolder != null) {
            final User user = BmobUser.getCurrentUser(getActivity(), User.class);
            if (TextUtils.isEmpty(user.getNickname())) {
                user.update(getActivity(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        User user1 = BmobUser.getCurrentUser(getActivity(), User.class);
                        itemHolder.setIcon(user1.getAvatar());
                        itemHolder.setTitle(user1.getNickname());
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        log("* 查询本地会话 获取个人信息+失败+错误码=" + i + " msg=" + s);
                    }
                });
            } else {
                itemHolder.setIcon(user.getAvatar());
                itemHolder.setTitle(user.getNickname());
            }
        }

        /**
         * 虚拟延时加载
         */
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3 * 1000);
                    runOnMain(new Runnable() {
                        @Override
                        public void run() {
                            sw_refresh.setRefreshing(false);
                            ChangeOtherManagerUpdate();
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }


    /**
     * 注册消息接收事件
     *
     * @param event 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     *              2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {


        //重新获取本地消息并刷新列表
        adapter.bindDatas(BmobIM.getInstance().loadAllConversation());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    @Override
    public void clearItemSelect() {
        List<ConversationHolder> l = new ArrayList(itemHolderLong.values());
        for (ConversationHolder co : l) {
            co.setIs_touch(false);
            co.delete.setVisibility(TextView.GONE);
        }
        itemHolderLong.clear();
        rebackRight();
    }

    @Override
    public void ChangeOtherManagerUpdate() {

        /**
         * 处理显示昵称，不要显示UserName字段，如果是微博授权 UserName字段则是一串16进制数，第一次登录获取微博昵称
         */

        log("ChangeOtherManagerUpdate  ConversationFragment类");
        BmobIM.getInstance().loadAllConversation();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        CopyOnWriteArrayList<BmobIMConversation> copylist = new CopyOnWriteArrayList<>();
        copylist.addAll(list);
        LetTalkApplication instance = LetTalkApplication.INSTANCE();
        for (BmobIMConversation b : copylist) {
            User user = instance.getUser(b.getConversationId());
            if (user != null) {
                if (!TextUtils.isEmpty(user.getNickname())) {
                    b.setConversationTitle(user.getNickname());
                    log("  b.setConversationTitle(user.getNickname()); Nick==" + user.getNickname() + "  ID" + b.getConversationId());
                }
            }
        }
        adapter.bindDatas(list);
        adapter.notifyDataSetChanged();
    }


/**
 private HashMap<Integer, Object> map = new HashMap<Integer, Object>();
 public Handler handler = new Handler() {
@Override public void handleMessage(Message msg) {
if (msg.what == 100) {
if (map.containsKey(msg.arg1)) {
log("Handld方法map.containsKey(msg.arg1)" + map.size());
ConversationHolder ch = (ConversationHolder) map.get(msg.arg1);
if (ch != null && ch.is_longClick()) {
ch.setIs_longClick(false);
ch.delete.setVisibility(TextView.GONE);
log("Handld方法ch!=null&&ch.is_longClick() 删除中");
}
map.remove(msg.arg1);
} else {

}
}

}
};
 */
}
