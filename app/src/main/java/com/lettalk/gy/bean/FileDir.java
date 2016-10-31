package com.lettalk.gy.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016-07-11.
 */
public class FileDir extends BmobObject {

    private String url;
    private BmobFile bmobfile;
    private Post post;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BmobFile getBmobfile() {
        return bmobfile;
    }

    public void setBmobfile(BmobFile bmobfile) {
        this.bmobfile = bmobfile;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
