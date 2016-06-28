package com.lettalk.gy.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lettalk.gy.bean.WeiBoBean;
import com.lettalk.gy.config.Config;
import com.lettalk.gy.model.i.WeiBoInfoListener;
import com.lettalk.gy.util.GsonTools;
import com.lettalk.gy.util.sina.Constants;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.net.NetUtils;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * 基类
 *
 * @author :smile
 * @project:BaseActivity
 * @date :2016-01-15-18:23
 */
public class BaseActivity extends FragmentActivity {


    protected int mScreenWidth;
    protected int mScreenHeight;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        initView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        initView();
    }

    @Subscribe
    public void onEvent(Boolean empty) {

    }

    protected void initView() {
    }

    protected void runOnMain(Runnable runnable) {
        runOnUiThread(runnable);
    }

    protected final static String NULL = "";
    private Toast toast;

    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(BaseActivity.this, NULL, Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘-一般是EditText.getWindowToken()
     *
     * @param token
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Log日志
     *
     * @param msg
     */
    public void log(String msg) {
        log(this.getPackageName(), msg);
       /* if (Config.DEBUG) {
            Logger.i(this.getPackageName(), msg);
        }*/
    }

    /**
     * Log日志
     *
     * @param msg
     */
    public void log(String TDA, String msg) {
        if (Config.DEBUG) {
            Logger.i(TDA, msg);
        }
    }


    // 依次类推，想要获取QQ或者新浪微博其他的信息，开发者可自行根据官方提供的API文档，传入对应的参数即可
    // QQ的API文档地址：http://wiki.open.qq.com/wiki/website/API%E5%88%97%E8%A1%A8
    // 微博的API文档地址：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI

    /**
     * 获取微博的资料
     *
     * @param
     * @param json
     * @return void
     * @throws
     * @Title: getWeiboInfo
     * @Description: TODO
     */

    public void getWeiboInfo(final JSONObject json, final WeiBoInfoListener boInfoListener) {
        // 根据http://open.weibo.com/wiki/2/users/show提供的API文档

        new Thread() {
            @Override
            public void run() {
                try {
                    WeiboParameters params = new WeiboParameters(Constants.APP_KEY);
                    if (json != null) {
                        params.put("access_token", json.getJSONObject("weibo").getString("access_token"));// 此为微博登陆成功之后返回的access_token
                        params.put("uid", json.getJSONObject("weibo").getString("uid"));// 此为微博登陆成功之后返回的uid
                    }
                    String result = NetUtils.internalHttpRequest(BaseActivity.this, "https://api.weibo.com/2/users/show.json", "GET", params);
                    WeiBoBean boBean = GsonTools.changeGsonToBean(result, WeiBoBean.class);
                    if (boBean != null) {
                        Log.i("login", ">>>>>>>微博的个人信息：根据http://open.weibo.com/wiki/2/users/show提供的API文档" + boBean.toString());
                        boInfoListener.getInfoSuccess(boBean);
                    } else {
                        boInfoListener.getInfoFail("获取失败");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        }.start();
    }


}
