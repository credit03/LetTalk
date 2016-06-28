package com.lettalk.gy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.lettalk.gy.R;
import com.lettalk.gy.base.BaseActivity;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.model.UserModel;

import cn.bmob.v3.listener.UpdateListener;

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
                    user.update(SplashActivity.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            log("user.update 更新资料成功");
                            startActivity(MainActivity.class, null, true);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            /**
                             *
                             */
                            log("user.update 更新资料失败");
                            toast("更新资料失败");
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("update", false);
                            startActivity(MainActivity.class, bundle, true);
                        }
                    });

                }
            }
        }, 1000);

    }
}
