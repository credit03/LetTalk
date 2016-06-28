package com.lettalk.gy.config;

/**
 * @author :smile
 * @project:Config
 * @date :2016-01-15-18:23
 */
public class Config {
    /**
     * Bmob应用key
     */
//    public static final String DEFAULT_APPKEY="d6f44e8f1ba9d3dcf4fab7a487fa97dd";//内
    public static final String DEFAULT_APPKEY="c94938255ebff5d607804df4868bc679";//外
    public static final String ID="a5c97ccc40c6fabfb328d83e8f24a415";//外
    public static final boolean DEBUG=true;
    //好友请求：已添加
    public static final int STATUS_VERIFIED=1;
    //好友请求：未读-未添加-接收到的初始状态
    public static final int STATUS_VERIFY_NONE=0;
    //好友请求：已读-未添加-点击查看了新朋友，则都变成已读状态
    public static final int STATUS_VERIFY_READED=2;
    //好友请求：拒绝
    public static final int STATUS_VERIFY_REFUSE=3;

}
