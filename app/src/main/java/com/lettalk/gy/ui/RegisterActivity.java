package com.lettalk.gy.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.base.ParentWithNaviActivity;
import com.lettalk.gy.event.FinishEvent;
import com.lettalk.gy.model.BaseModel;
import com.lettalk.gy.model.UserModel;
import com.lettalk.gy.util.SharePrefUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * 注册界面
 *
 * @author :smile
 * @project:RegisterActivity
 * @date :2016-01-15-18:23
 */
public class RegisterActivity extends ParentWithNaviActivity {

    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.btn_register)
    Button btn_register;

    /**
     * 院系
     */
    @Bind(R.id.but_tm)
    Button btn_tm;
    @Bind(R.id.et_tm)
    TextView et_tm;

    @Bind(R.id.et_password_again)
    EditText et_password_again;


    private String phone, tm_back = "管理系";

    private String[] tm = {"机电系", "计算机系", "管理系", "财经系", "外语系", "热作系", "商务系",
            "艺术系", "德国F+U", "国际交流学院"};
    private int tm_ChoiceItem = 0;

    @Override
    protected String title() {
        return "注册";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initNaviView();
        phone = getIntent().getStringExtra("phone");
        toast("手机号码+"+phone);

    }


    @OnClick(R.id.btn_register)
    public void onRegisterClick(View view) {

        UserModel.getInstance().register(et_username.getText().toString(), phone, et_password.getText().toString(), et_password_again.getText().toString(), tm_back, new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    EventBus.getDefault().post(new FinishEvent());
                    SharePrefUtil.saveBoolean(RegisterActivity.this, "frist_start", true);

                    startActivity(MainActivity.class, null, true);
                    // toast("邮箱认证登录");
                } else {
                    if (e.getErrorCode() == BaseModel.CODE_NOT_EQUAL) {
                        et_password_again.setText("");
                    }
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    @OnClick(R.id.but_tm)
    public void onTmClock(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("农工商院系");

        builder.setSingleChoiceItems(tm, tm_ChoiceItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        tm_back = tm[which];
                        et_tm.setText(tm_back);
                        tm_ChoiceItem = which;
                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

}
