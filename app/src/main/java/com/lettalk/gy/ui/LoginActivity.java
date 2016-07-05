package com.lettalk.gy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lettalk.gy.R;
import com.lettalk.gy.base.BaseActivity;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.event.FinishEvent;
import com.lettalk.gy.model.UserModel;
import com.lettalk.gy.util.mob.ui.RegisterPage;
import com.lettalk.gy.util.sina.AccessTokenKeeper;
import com.lettalk.gy.util.sina.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class LoginActivity extends BaseActivity {
    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.tv_register)
    TextView tv_register;

    @Bind(R.id.tv_findpw)
    TextView tv_findpw;


    @Bind(R.id.et_phone)
    EditText phone;
    @Bind(R.id.et_vrify)
    EditText et_inverify;
    @Bind(R.id.but_verify)
    Button btn_getvrify;
    @Bind(R.id.btn_next)
    Button btn_next;

    @Bind(R.id.input1)
    LinearLayout input1;

    @Bind(R.id.input2)
    LinearLayout input2;
    @Bind(R.id.iv_sms)
    ImageView ivSms;
    @Bind(R.id.iv_sina)
    ImageView ivSina;
    @Bind(R.id.tv_sina)

    TextView tvSina;
    @Bind(R.id.tv_sms)
    TextView tvSms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);



     /* // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {

        }*/
    }

    /**
     * 普通登录
     *
     * @param view
     */
    @OnClick(R.id.btn_login)
    public void onLoginClick(View view) {
        String s = et_username.getText().toString();
        if (TextUtils.isEmpty(s)) {
            toast("用户名不能为空");
            return;
        }
        if (s.length() >= 11 && isNum(s)) {

            /**
             * 通过手机登录
             */
            UserModel.getInstance().LoginByPhone(this, s, et_password.getText().toString(), new LogInListener() {

                @Override
                public void done(Object o, BmobException e) {
                    if (e == null && o != null) {
                        User user = (User) o;
                        BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                        startActivity(MainActivity.class, null, true);
                    } else {
                        toast("帐号或密码错误");
                        //  toast(e.getMessage() + e.getErrorCode());
                    }
                }

            });
        } else {

            UserModel.getInstance().login(s, et_password.getText().toString(), new LogInListener() {
                @Override
                public void done(Object o, BmobException e) {
                    if (e == null) {
                        User user = (User) o;
                        BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                        startActivity(MainActivity.class, null, true);
                    } else {
                        toast("帐号或密码错误");
                    }
                }
            });
        }


    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    private boolean isinput1 = true;

    /**
     * 快速登录与普通登录模式切换
     *
     * @param viwe
     */
    @OnClick(R.id.iv_sms)
    public void inputSwitch(View viwe) {

        if (isinput1) {
            input1.setVisibility(View.INVISIBLE);
            input2.setVisibility(View.VISIBLE);
            ivSms.setImageResource(R.mipmap.ic_launcher);
            tvSms.setText("普通登录");
            isinput1 = false;
        } else {
            input1.setVisibility(View.VISIBLE);
            input2.setVisibility(View.INVISIBLE);
            ivSms.setImageResource(R.drawable.sms_logo);
            tvSms.setText("快速登录");
            isinput1 = true;
        }
    }


    /**
     * 注册模块
     *
     * @param view
     */
    @OnClick(R.id.tv_register)
    public void onRegisterClick(View view) {

        // startActivity(RegisterActivity.class, null, false);
        // SMSSDKPage();
        BmobSMSVrify();
    }

    /**
     * BmobSMS SDK认证
     */
    private void BmobSMSVrify() {
        startActivity(BmobSMSVrify.class, null, false);
    }

    /***
     * Mob手机短信SDK验证
     */
    private void SMSSDKPage() {
        SMSSDK.initSDK(this, this.getString(R.string.mob_smskey),
                this.getString(R.string.mob_smssecret));


        // 打开注册页面
        RegisterPage registerPage = new RegisterPage();

        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");

                    // 提交用户信息
                    // registerUser(country, phone);
                    Intent intent = new Intent(LoginActivity.this,
                            RegisterActivity.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("country", country);
                    startActivity(intent);
                }
            }
        });

        registerPage.show(this);
    }

    /**
     * 忘记密码，通过手机号码重置密码
     *
     * @param view
     */
    @OnClick(R.id.tv_findpw)
    public void FindPassWrod(View view) {
        startActivity(FindPwActivity.class, null, false);
    }

    @Subscribe
    public void onEventMainThread(FinishEvent event) {
        finish();
    }


    /**
     * 短信快速登录
     */

    private String pnum;

    /**
     * 倒计时59秒
     */
    private int time = 0;

    Handler myTime = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 100) {
                if (time++ < 59) {
                    btn_getvrify.setText("已发送（" + (59 - time) + "）");
                    myTime.sendEmptyMessageDelayed(100, 1000);
                } else {
                    btn_getvrify.setText("重发验证码");
                    SendVrify = false;
                    time = 0;
                }
            }

            return true;
        }
    });
    /**
     * 是否发送了验证码
     */
    private boolean SendVrify = false;

    @OnClick(R.id.but_verify)
    public void OnVrify(View v) {


        pnum = phone.getText().toString().trim();

        if (!TextUtils.isEmpty(pnum)) {
            BmobSMS.requestSMSCode(this, pnum, "sms", new RequestSMSCodeListener() {

                @Override
                public void done(Integer smsId, cn.bmob.sms.exception.BmobException ex) {
                    // TODO Auto-generated method stub
                    if (ex == null) {//验证码发送成功
                        toast("验证码发送成功");
                        btn_getvrify.setText("已发送（59）");
                        SendVrify = true;
                        /**
                         * 启动定时器59
                         */
                        myTime.sendEmptyMessage(100);

                        // toast("解锁next触发");
                        btn_next.setEnabled(true);
                    } else {
                        if (ex.getErrorCode() == 10010) {
                            toast("一天内同一手机号发送的短信不能超过10条");
                            return;
                        }
                        Toast.makeText(LoginActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            toast("请输入手机号码噢");
        }

    }

    /**
     * 快速登录，确定登录
     *
     * @param v
     */
    @OnClick(R.id.btn_next)
    public void OnNext(View v) {

        String vriCode = et_inverify.getText().toString().trim();
        if (!TextUtils.isEmpty(vriCode)) {
            BmobUser.loginBySMSCode(LoginActivity.this, pnum, vriCode, new LogInListener<User>() {

                @Override
                public void done(User user, BmobException e) {
                    // TODO Auto-generated method stub
                    if (user != null) {
                        BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                        startActivity(MainActivity.class, null, true);
                    } else {
                        toast("快速登录失败");
                    }
                }
            });
        } else {
            toast("请输入验证码");
        }
    }


    /*********************
     * 微博登录
     *********************/
    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    @OnClick(R.id.iv_sina)
    public void sinaLoginClick(View v) {
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String phoneNum = mAccessToken.getPhoneNum();

            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                toast("微博授权成功回调");
                String uid = mAccessToken.getUid();
                String token = mAccessToken.getToken();
                long expiresTime = mAccessToken.getExpiresTime();
                /**
                 * BmobThirdUserAuth的各参数解释：
                 1、snsType:只能是三种取值中的一种：weibo、qq、weixin
                 2、accessToken：接口调用凭证
                 3、expiresIn：access_token的有效时间
                 4、userId:用户身份的唯一标识，对应微博授权信息中的uid,对应qq和微信授权信息中的openid
                 */
                final BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("weibo", token, Long.toString(expiresTime), uid);

                BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

                    @Override
                    public void onSuccess(JSONObject userAuth) {
                        toast("第三方登陆授权成功");
                        Log.i("smile", authInfo.getSnsType() + "登陆成功返回:" + userAuth.toString());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("json", userAuth.toString());
                        intent.putExtra("from", authInfo.getSnsType());
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        Log.i("smile", "第三方登陆失败：" + msg);
                        toast("第三方登陆失败");
                    }

                });


            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                toast("当您未在平台上注册的应用程序的包名与签名时" + code);
            }

        }

        @Override
        public void onCancel() {

            toast("授权取消");
        }

        @Override
        public void onWeiboException(WeiboException e) {
            toast("授权失败");
        }
    }

    /**
     * 微博认证后回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }


    }


}
