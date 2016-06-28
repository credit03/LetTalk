package com.lettalk.gy.bean;

import com.lettalk.gy.db.NewFriend;

import cn.bmob.v3.BmobUser;

/**
 * @author :smile
 * @project:User
 * @date :2016-01-22-18:11
 */
public class User extends BmobUser {

    private String avatar;

    private String school_tm;

    public String getSchool_tm() {
        return school_tm;
    }

    public void setSchool_tm(String school_tm) {
        this.school_tm = school_tm;
    }

    /**
     * 用户昵称
     */
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 第一次授权？
     */
    private Boolean first_auth;

    public Boolean getFirst_auth() {
        return first_auth;
    }

    private Boolean sex = false;

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public void setFirst_auth(Boolean first_auth) {
        this.first_auth = first_auth;
    }


    private String userbg;

    public String getUserbg() {
        return userbg;
    }

    public void setUserbg(String userbg) {
        this.userbg = userbg;
    }


    private String Personalized;

    public String getPersonalized() {
        return Personalized;
    }

    public void setPersonalized(String personalized) {
        Personalized = personalized;
    }

    /**
     * 是否修改了
     */
    public boolean isEdit = false;


    public User() {
    }

    public User(NewFriend friend) {
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
