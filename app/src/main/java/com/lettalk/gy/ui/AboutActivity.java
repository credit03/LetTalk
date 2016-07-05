package com.lettalk.gy.ui;

import android.os.Bundle;
import android.webkit.WebView;

import com.lettalk.gy.R;
import com.lettalk.gy.base.ParentWithNaviActivity;
import com.lettalk.gy.util.dialog.XmlToHtml;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends ParentWithNaviActivity implements ParentWithNaviActivity.ToolBarListener {

    @Bind(R.id.wv_web)
    WebView wvWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initNaviView();
        ButterKnife.bind(this);

        XmlToHtml html = new XmlToHtml(this);
        wvWeb.loadData(html.show(), "text/html; charset=UTF-8", null);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        this.finish();
    }

    @Override
    public Object left() {
        return R.drawable.base_action_bar_back_bg_selector;
    }

    @Override
    public ToolBarListener setToolBarListener() {
        return this;
    }

    @Override
    protected String title() {
        return "关于";
    }

    @Override
    public void clickLeft() {
        this.finish();
    }

    @Override
    public void clickRight() {

    }
}
