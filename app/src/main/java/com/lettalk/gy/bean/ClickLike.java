package com.lettalk.gy.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016-07-06.
 */
public class ClickLike extends BmobObject {

    private User clickuser;//点赞用户
    private Post post;//点赞所属帖

    public User getClickuser() {
        return clickuser;
    }

    public void setClickuser(User clickuser) {
        this.clickuser = clickuser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
