package com.lettalk.gy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lettalk.gy.R;
import com.lettalk.gy.base.ParentWithNaviActivity;
import com.lettalk.gy.config.Config;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class BmobSMSVrify extends ParentWithNaviActivity {

    @Bind(R.id.et_phone)
    EditText phone;
    @Bind(R.id.et_vrify)
    EditText et_inverify;
    @Bind(R.id.but_verify)
    Button btn_getvrify;
    @Bind(R.id.btn_next)
    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmob_smsvrify);
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
                public void done(Integer smsId, BmobException ex) {
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
                        Toast.makeText(BmobSMSVrify.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            toast("请输入手机号码噢");
        }

    }

    @OnClick(R.id.btn_next)
    public void OnNext(View v) {

        String vriCode = et_inverify.getText().toString().trim();
        if (!TextUtils.isEmpty(vriCode)) {
            BmobSMS.verifySmsCode(this, pnum, vriCode, new VerifySMSCodeListener() {

                @Override
                public void done(BmobException ex) {
                    // TODO Auto-generated method stub
                    if (ex == null) {//短信验证码已验证成功

                       /* Bundle bundle = new Bundle();
                        bundle.putString("phone", pnum);
                        startActivity(RegisterActivity.class, bundle, false);*/
                        Intent intent = new Intent(BmobSMSVrify.this, RegisterActivity.class);
                        intent.putExtra("phone", pnum);
                        startActivity(intent);

                    } else {
                        btn_next.setEnabled(false);
                    }
                }
            });
        }


    }

    @Override
    protected String title() {
        return "手机验证注册";
    }
}
