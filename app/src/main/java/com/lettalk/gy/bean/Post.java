package com.lettalk.gy.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016-07-06.
 */
public class Post extends BmobObject {

    private String content;// 帖子内容

    private User author;//帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

    private Boolean haveimage = false;//帖子图片


    /**
     * 原子计数器
     * 很多应用可能会有计数器功能的需求，比如文章点赞的功能，如果大量用户并发操作，用普通的更新方法操作的话，会存在数据不一致的情况。
     * <p/>
     * 为此，Bmob提供了原子计数器来保证原子性的修改某一数值字段的值。注意：原子计数器只能对应用于Web后台的Number类型的字段，即JavaBeans数据对象中的Integer对象类型（不要用int类型）。
     */
    private Integer clickcount;
    private Integer commentcount;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    public Boolean getHaveimage() {
        return haveimage;
    }

    public void setHaveimage(Boolean haveimage) {
        this.haveimage = haveimage;
    }

    public Integer getClickcount() {
        return clickcount;
    }

    public void setClickcount(Integer clickcount) {
        this.clickcount = clickcount;
    }

    public Integer getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(Integer commentcount) {
        this.commentcount = commentcount;
    }
}
