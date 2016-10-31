package com.lettalk.gy.ui;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lettalk.gy.R;
import com.lettalk.gy.base.BaseActivity;
import com.lettalk.gy.base.BaseFragment;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.bean.WeiBoBean;
import com.lettalk.gy.db.NewFriendManager;
import com.lettalk.gy.event.RefreshEvent;
import com.lettalk.gy.model.lisetener.WeiBoInfoListener;
import com.lettalk.gy.ui.fragment.ContactFragment;
import com.lettalk.gy.ui.fragment.ConversationFragment;
import com.lettalk.gy.ui.fragment.FindFragment;
import com.lettalk.gy.ui.fragment.SetFragment;
import com.lettalk.gy.util.IMMLeaks;
import com.lettalk.gy.util.dialog.ChangeLogDialog;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author :smile
 * @project:MainActivity
 * @date :2016-01-15-18:23
 */
public class MainActivity extends BaseActivity implements ObseverListener {

    @Bind(R.id.btn_conversation)
    Button btn_conversation;
    @Bind(R.id.btn_set)
    Button btn_set;
/*
    @Bind(R.id.btn_contact)
    Button btn_contact;*/

    @Bind(R.id.iv_conversation_tips)
    ImageView iv_conversation_tips;

    @Bind(R.id.iv_contact_tips)
    ImageView iv_contact_tips;
    @Bind(R.id.btn_find)
    Button btn_find;

    private Button[] mTabs;
    private ConversationFragment conversationFragment;
    private SetFragment setFragment;
    private ContactFragment contactFragment;

    private FindFragment findFragment;
    private BaseFragment[] fragments;
    private int index;
    private int currentTabIndex;

    private String json = "";
    private String from = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //connect server
        Bundle bundle = getIntent().getExtras();
        final User user = BmobUser.getCurrentUser(this, User.class);
        if (bundle != null) {
            log("MainActivity >>bundle != null");
            boolean update = bundle.getBoolean("update");
            if (!update) {
                user.update(this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        initData(user);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        /**
                         * 更新失败
                         */
                        log("onFailure 更新失败");
                        initData(user);
                    }
                });
            }
        } else {
            log("MainActivity >>bundle == null 空 1024");
            initData(user);
        }

        showChangeLogDialog();


    }

    private void showChangeLogDialog() {
        ChangeLogDialog changeLogDialog = new ChangeLogDialog(this);
        changeLogDialog.show();
    }

    private void initData(User user) {

        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("connect success");
                } else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                toast("" + status.getMsg());
            }
        });
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());
        json = getIntent().getStringExtra("json");
        from = getIntent().getStringExtra("from");
        /**
         * 从微博上获取用户资料更新
         */
        getWeiBoInfo(json);


    }


    /**
     * 获取微博信息
     */
    private void getWeiBoInfo(String json) {
        if (!TextUtils.isEmpty(json)) {
            final User currentUser = BmobUser.getCurrentUser(this, User.class);

            log("查看用户是否第一次授权 +" + currentUser.getFirst_auth());
            if (currentUser.getFirst_auth()) {
                JSONObject js = null;
                try {
                    js = new JSONObject(json);
                    getWeiboInfo(js, new WeiBoInfoListener() {
                        @Override
                        public void getInfoSuccess(WeiBoBean bean) {

                            /**
                             * 设置昵称
                             */
                            String nickname = currentUser.getNickname();
                            if (TextUtils.isEmpty(nickname)) {
                                currentUser.setNickname(bean.getScreen_name());
                            }

                            /**
                             * 设置姓名
                             */
                            String avatar = currentUser.getAvatar();
                            if (TextUtils.isEmpty(avatar)) {
                                currentUser.setAvatar(bean.getAvatar_hd());
                            }
                            String sex = bean.getGender();
                            if (currentUser.getSex()) {
                                if (sex.equals("m")) {
                                    currentUser.setSex(true);
                                } else {
                                    currentUser.setSex(false);
                                }
                            }


                            /**
                             *微博授权，默认设置为"尚未设置院系"
                             */
                            currentUser.setSchool_tm("尚未设置院系");
                            currentUser.setFirst_auth(false);
                            currentUser.update(MainActivity.this, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    toast("更新资料成功");
                                    log("更新资料成功 +");

                                    /**
                                     * 更新设置页面昵称
                                     */
                                    SetFragment setFragment = (SetFragment) fragments[2];
                                    setFragment.upDateNickName();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    toast("更新资料失败" + s);
                                    log("更新资料失败 +");
                                }
                            });

                        }

                        @Override
                        public void getInfoFail(String err) {
                            toast(err);
                            log(err);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                toast("非第一次授权");
                log("非第一次授权");
            }
        }

    }


    @Override
    protected void initView() {
        super.initView();
        mTabs = new Button[3];
        mTabs[0] = btn_find;
        mTabs[1] = btn_conversation;
        mTabs[2] = btn_set;
        mTabs[1].setSelected(true);
        currentTabIndex = 1;
        initTab();
    }

    private void initTab() {
        conversationFragment = new ConversationFragment();
        contactFragment = new ContactFragment();
        contactFragment.setInterObservable(conversationFragment);
        setFragment = new SetFragment();
        findFragment = new FindFragment();
        fragments = new BaseFragment[]{findFragment, conversationFragment, setFragment, contactFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, findFragment)
                .add(R.id.fragment_container, conversationFragment)
                .add(R.id.fragment_container, setFragment)
                .add(R.id.fragment_container, contactFragment)
                .hide(findFragment).hide(setFragment).hide(contactFragment)
                .show(conversationFragment).commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onTabSelect(View view) {
        switch (view.getId()) {
            case R.id.btn_find:
                index = 0;
                break;
            case R.id.btn_conversation:
                index = 1;
                if (is_contacts) {
                    setButtonTheme(1);
                }
                break;
            case R.id.btn_set:
                index = 2;
                break;
        }
        onTabIndex(index);
    }


    private boolean is_contacts = false;

    private void onTabIndex(int index) {

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            if (is_contacts) {
                setButtonTheme(1);
                trx.hide(fragments[3]);
            } else {
                trx.hide(fragments[currentTabIndex]);
            }
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }

            trx.show(fragments[index]).commit();

            //判断已打开的联系人,再次点击返回会话
        } else if (is_contacts) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            if (is_contacts) {
                trx.hide(fragments[3]);
            }
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }

            checkRedPoint();
            /**
             * 显示会话
             */
            trx.show(fragments[1]).commit();

        }

        if (index == 3) {
            is_contacts = true;
            mTabs[currentTabIndex].setSelected(false);
            mTabs[1].setSelected(true);
            currentTabIndex = 1;
        } else {
            is_contacts = false;
            mTabs[currentTabIndex].setSelected(false);
            mTabs[index].setSelected(true);
            currentTabIndex = index;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示小红点
        checkRedPoint();
        //添加观察者-用于是否显示通知消息
        BmobNotificationManager.getInstance(this).addObserver(this);
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //移除观察者
        BmobNotificationManager.getInstance(this).removeObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
        //完全退出应用时需调用clearObserver来清除观察者
        BmobNotificationManager.getInstance(this).clearObserver();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        log("---主页接收到自定义消息---");
        checkRedPoint();
    }

    private void checkRedPoint() {
        if (BmobIM.getInstance().getAllUnReadCount() > 0) {
            iv_conversation_tips.setVisibility(View.VISIBLE);
        } else {
            iv_conversation_tips.setVisibility(View.GONE);
        }
        //是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
            iv_contact_tips.setVisibility(View.VISIBLE);
        } else {
            iv_contact_tips.setVisibility(View.GONE);
        }
    }

    /**
     * 切换主题
     *
     * @param index
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void setButtonTheme(int index) {
        if (index == 1) {
            fragments[3].clearItemSelect();
            mTabs[1].setText("会话");
            Drawable top = MainActivity.this.getResources().getDrawable(R.drawable.tab_message_btn);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
            mTabs[1].setCompoundDrawables(null, top, null, null);
        }
    }

    /**
     * 返回上一级的页面
     *
     * @param pageIndex
     */

    public void back_upPage(int pageIndex) {

        checkRedPoint();
        setButtonTheme(pageIndex);
        onTabIndex(pageIndex);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void goToContacts() {
        /**
         * 关闭小红点
         */
        iv_conversation_tips.setVisibility(View.GONE);
        onTabIndex(3);
        mTabs[1].setText("联系人(点击返回会话)");
        Drawable top = getResources().getDrawable(R.drawable.tab_contact_btn);
        top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
        mTabs[1].setCompoundDrawables(null, top, null, null);
    }


    private long lasttime = 0;


    @Override
    public void onBackPressed() {
        if (is_contacts) {
            back_upPage(1);
        } else {
           /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("是否退出").setIcon(R.mipmap.ic_launcher).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    MainActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();*/

            if (lasttime == 0 || lasttime + 2 * 1000 < System.currentTimeMillis()) {
                toast("再次点击退出");
                lasttime = System.currentTimeMillis();
            } else {
                MainActivity.super.onBackPressed();
            }

        }
    }
}
