package com.lettalk.gy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lettalk.gy.R;
import com.lettalk.gy.base.ParentWithNaviActivity;
import com.lettalk.gy.config.Config;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;

public class FindPwActivity extends ParentWithNaviActivity {


    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_vrify)
    EditText etVrify;
    @Bind(R.id.but_verify)
    Button butVerify;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.et_pw)
    EditText etPw;
    @Bind(R.id.et_pw_again)
    EditText etPwAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);
        ButterKnife.bind(this);
        initNaviView();
        BmobSMS.initialize(this, Config.ID);
    }

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
                    butVerify.setText("已发送（" + (59 - time) + "）");
                    myTime.sendEmptyMessageDelayed(100, 1000);
                } else {
                    butVerify.setText("重发验证码");
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


        pnum = etPhone.getText().toString().trim();

        if (!TextUtils.isEmpty(pnum)) {

            BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();

            query.addWhereEqualTo("mobilePhoneNumber", pnum);
            query.findObjects(this, new FindListener<BmobUser>() {
                @Override
                public void onSuccess(List<BmobUser> object) {
                    // TODO Auto-generated method stub


                    if (object.size() == 1) {

                        BmobSMS.requestSMSCode(FindPwActivity.this, pnum, "repw", new RequestSMSCodeListener() {

                            @Override
                            public void done(Integer smsId, BmobException ex) {
                                // TODO Auto-generated method stub
                                if (ex == null) {//验证码发送成功
                                    toast("验证码发送成功");
                                    butVerify.setText("已发送（59）");
                                    SendVrify = true;
                                    /**
                                     * 启动定时器59
                                     */
                                    myTime.sendEmptyMessage(100);
                                    // toast("解锁next触发");
                                    btnNext.setEnabled(true);
                                    etPw.setEnabled(true);
                                    etPwAgain.setEnabled(true);
                                } else {
                                    if (ex.getErrorCode() == 10010) {
                                        toast("一天内同一手机号发送的短信不能超过10条");
                                        return;
                                    }
                                    Toast.makeText(FindPwActivity.this, "验证码发送失败" + ex.getErrorCode(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        toast("没有当前用户" + object.size());
                    }

                }

                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    toast("没有当前用户：" + msg);
                }
            });

        } else {
            toast("请输入手机号码噢");
        }

    }

    @OnClick(R.id.btn_next)
    public void OnNext(View v) {

        String vriCode = etVrify.getText().toString().trim();
        if (TextUtils.isEmpty(vriCode)) {
            toast("请输入验证码");
            return;
        }
        String pw1 = etPw.getText().toString().trim(), pw2 = etPwAgain.getText().toString().trim();
        if (TextUtils.isEmpty(pw1) || TextUtils.isEmpty(pw2)) {
            toast("请输入密码");
            return;
        }

        if (!pw1.equals(pw2)) {
            toast("密码不一致");
            return;
        }

        BmobUser.resetPasswordBySMSCode(this, vriCode, pw1, new ResetPasswordByCodeListener() {


            @Override
            public void done(cn.bmob.v3.exception.BmobException e) {
                // TODO Auto-generated method stub
                if (e == null) {
                    Log.i("smile", "密码重置成功");
                    toast("重置密码成功");
                    FindPwActivity.this.finish();

                } else {
                    toast("重置密码失败");
                    Log.i("smile", "重置失败：code =" + e.getErrorCode() + ",msg = " + e.getLocalizedMessage());
                }
            }
        });

    }

    @Override
    protected String title() {
        return "通过手机号码重置密码";
    }
}
