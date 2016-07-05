package com.lettalk.gy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.lettalk.gy.R;
import com.lettalk.gy.base.BaseActivity;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.model.UserModel;
import com.lettalk.gy.util.sina.AccessTokenKeeper;
import com.lettalk.gy.util.sina.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.openapi.RefreshTokenApi;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(LoginActivity.class, null, true);
                } else {
                    Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(SplashActivity.this);
                    if (oauth2AccessToken.isSessionValid()) {
                        log("Refresh Token刷新授权");
                        RefreshTokenApi.create(getApplicationContext()).refreshToken(
                                Constants.APP_KEY, oauth2AccessToken.getRefreshToken(), new RequestListener() {

                                    @Override
                                    public void onWeiboException(WeiboException arg0) {
                                        log("通过Refresh Token刷新授权有效期fail");

                                    }

                                    @Override
                                    public void onComplete(String arg0) {
                                        log("通过Refresh Token刷新授权有效期 succee");
                                        Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(arg0);
                                        AccessTokenKeeper.writeAccessToken(SplashActivity.this, mAccessToken);

                                        //  Toast.makeText(SplashActivity.this, "RefreshToken Result : " + arg0, Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        log("不用刷新刷新授权");
                    }


                    BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                    startActivity(MainActivity.class, null, true);


                }
            }
        }, 1000);

    }
}
