package com.lettalk.gy.model.i;

import com.lettalk.gy.bean.WeiBoBean;

/**
 * Created by Administrator on 2016-05-25.
 */
public interface WeiBoInfoListener {

    public abstract void getInfoSuccess(WeiBoBean bean);
    public abstract void getInfoFail(String err);
}
