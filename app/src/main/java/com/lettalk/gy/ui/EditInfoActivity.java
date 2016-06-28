package com.lettalk.gy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lettalk.gy.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditInfoActivity extends AppCompatActivity implements TextWatcher {

    @Bind(R.id.toolbar_profile_back)
    ImageView toolbarProfileBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.tv_point)
    TextView tvPoint;
    @Bind(R.id.bnt_save)
    Button bntSave;


    private String title;
    private String name;
    private String dec;
    private int code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        name = getIntent().getStringExtra("name");
        dec = getIntent().getStringExtra("dec");
        code = getIntent().getIntExtra("code", 0);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        if (code == 0x104) {
            //textMultiLine\
            etInput.setMaxEms(15);
            etInput.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            etInput.setLines(3);
        }


        if (!TextUtils.isEmpty(name)) {
            etInput.setText(name);
            etInput.addTextChangedListener(this);
        }

        if (!TextUtils.isEmpty(dec)) {
            tvPoint.setText(dec);
        }

    }


    @OnClick(R.id.bnt_save)
    public void Save(View v) {
        if (TextUtils.isEmpty(etInput.getText())) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (etInput.getText().equals(name)) {
            this.finish();
        }
        Intent intent = new Intent(this, UserInfoAcivityPro.class);
        intent.putExtra("DEC", etInput.getText().toString().trim());

        this.setResult(code, intent);
        this.finish();

    }


    @OnClick(R.id.toolbar_profile_back)
    public void Back(View v) {

        this.finish();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!s.toString().toString().equals(name)) {
            bntSave.setEnabled(true);
        } else {
            bntSave.setEnabled(false);
        }
    }


}
