package com.lettalk.gy.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * 帖子包集合
 * 包括信息内容，评论内容，点赞好友，评论个数，点赞个数
 * Created by Administrator on 2016-07-10.
 */
public class FindBag {

    private Post post;
    private ClickLike clickLike;
    private List<FileDir> fileDirs;
    private Comment comment;


    private String errcode;
    private String errmsg;

    public Post getPost() {

        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public ClickLike getClickLike() {
        return clickLike;
    }

    public void setClickLike(ClickLike clickLike) {
        this.clickLike = clickLike;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }


    public List<FileDir> getFileDirs() {
        return fileDirs;
    }

    public void setFileDirs(List<FileDir> fileDirs) {
        this.fileDirs = fileDirs;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }


    public void AppendErrInfo(String type, String code, String msg) {
        if (TextUtils.isEmpty(getErrcode())) {
            setErrcode(type + " Code：" + code);
        } else {
            setErrcode(getErrcode() + "," + type + "Code：" + code);

        }

        if (TextUtils.isEmpty(getErrmsg())) {
            setErrmsg(type + " 失败内容：" + msg);
        } else {
            setErrmsg(getErrmsg() + "," + type + "失败内容：" + msg);

        }
    }


    @Deprecated
    private int ClickCount;
    @Deprecated
    private int CommentCount;

    @Deprecated
    public int getClickCount() {
        return ClickCount;
    }

    @Deprecated
    public void setClickCount(int clickCount) {
        ClickCount = clickCount;
    }

    @Deprecated
    public int getCommentCount() {
        return CommentCount;
    }

    @Deprecated
    public void setCommentCount(int commentCount) {
        CommentCount = commentCount;
    }

}
