package com.lettalk.gy.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016-05-25.
 */
public class WeiBoBean {


    /**
     * allow_all_act_msg : false
     * allow_all_comment : true
     * avatar_hd : http://tva2.sinaimg.cn/crop.0.0.1080.1080.1024/005P1PmMjw8elfo42exzgj30u00u03z5.jpg
     * avatar_large : http://tva2.sinaimg.cn/crop.0.0.1080.1080.180/005P1PmMjw8elfo42exzgj30u00u03z5.jpg
     * bi_followers_count : 1
     * block_app : 0
     * block_word : 0
     * city : 1
     * class : 1
     * created_at : Sat Oct 18 21:56:57 +0800 2014
     * credit_score : 80
     * description : 干自己喜欢的事情
     * domain :
     * favourites_count : 0
     * follow_me : false
     * followers_count : 15
     * following : false
     * friends_count : 29
     * gender : m
     * geo_enabled : true
     * id : 5334693080
     * idstr : 5334693080
     * lang : zh-cn
     * location : 广东 广州
     * mbrank : 0
     * mbtype : 0
     * name : credit毅
     * online_status : 0
     * pagefriends_count : 0
     * profile_image_url : http://tva2.sinaimg.cn/crop.0.0.1080.1080.50/005P1PmMjw8elfo42exzgj30u00u03z5.jpg
     * profile_url : u/5334693080
     * province : 44
     * ptype : 0
     * remark :
     * screen_name : credit毅
     * star : 0
     * status : {"attitudes_count":0,"biz_feature":0,"comments_count":0,"created_at":"Wed Apr 20 21:00:57 +0800 2016","darwin_tags":[],"favorited":false,"hot_weibo_tags":[],"id":3966443379042197,"idstr":"3966443379042197","in_reply_to_screen_name":"","in_reply_to_status_id":"","in_reply_to_user_id":"","isLongText":false,"mid":"3966443379042197","mlevel":0,"page_type":32,"pic_urls":[],"reposts_count":0,"source":"","source_allowclick":0,"source_type":1,"text":"友谊的小船说翻就翻，#乐视新品#说来就来！4月26日，乐2、乐Max 2、乐2 Pro震撼首发~@乐视商城 现货预售+全场免邮，8大生态权益买就送！一键预约并分享，抽送乐2手机哦~@赵一成_http://t.cn/RqX6Wts","textLength":184,"text_tag_tips":[],"truncated":false,"userType":0,"visible":{"list_id":0,"type":0}}
     * statuses_count : 4
     * urank : 4
     * url :
     * user_ability : 0
     * verified : false
     * verified_reason :
     * verified_reason_url :
     * verified_source :
     * verified_source_url :
     * verified_trade :
     * verified_type : -1
     * weihao :
     */

    private boolean allow_all_act_msg;
    private boolean allow_all_comment;
    private String avatar_hd;
    private String avatar_large;
    private int bi_followers_count;
    private int block_app;
    private int block_word;
    private String city;
    @SerializedName("class")
    private int classX;
    private String created_at;
    private int credit_score;
    private String description;
    private String domain;
    private int favourites_count;
    private boolean follow_me;
    private int followers_count;
    private boolean following;
    private int friends_count;
    private String gender;
    private boolean geo_enabled;
    private long id;
    private String idstr;
    private String lang;
    private String location;
    private int mbrank;
    private int mbtype;
    private String name;
    private int online_status;
    private int pagefriends_count;
    private String profile_image_url;
    private String profile_url;
    private String province;
    private int ptype;
    private String remark;
    private String screen_name;
    private int star;
    /**
     * attitudes_count : 0
     * biz_feature : 0
     * comments_count : 0
     * created_at : Wed Apr 20 21:00:57 +0800 2016
     * darwin_tags : []
     * favorited : false
     * hot_weibo_tags : []
     * id : 3966443379042197
     * idstr : 3966443379042197
     * in_reply_to_screen_name :
     * in_reply_to_status_id :
     * in_reply_to_user_id :
     * isLongText : false
     * mid : 3966443379042197
     * mlevel : 0
     * page_type : 32
     * pic_urls : []
     * reposts_count : 0
     * source :
     * source_allowclick : 0
     * source_type : 1
     * text : 友谊的小船说翻就翻，#乐视新品#说来就来！4月26日，乐2、乐Max 2、乐2 Pro震撼首发~@乐视商城 现货预售+全场免邮，8大生态权益买就送！一键预约并分享，抽送乐2手机哦~@赵一成_http://t.cn/RqX6Wts
     * textLength : 184
     * text_tag_tips : []
     * truncated : false
     * userType : 0
     * visible : {"list_id":0,"type":0}
     */

    private StatusBean status;
    private int statuses_count;
    private int urank;
    private String url;
    private int user_ability;
    private boolean verified;
    private String verified_reason;
    private String verified_reason_url;

    private String verified_source;
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    private String verified_source_url;
    private String verified_trade;
    private int verified_type;
    private String weihao;

    public boolean isAllow_all_act_msg() {
        return allow_all_act_msg;
    }

    public void setAllow_all_act_msg(boolean allow_all_act_msg) {
        this.allow_all_act_msg = allow_all_act_msg;
    }

    public boolean isAllow_all_comment() {
        return allow_all_comment;
    }

    public void setAllow_all_comment(boolean allow_all_comment) {
        this.allow_all_comment = allow_all_comment;
    }

    public String getAvatar_hd() {
        return avatar_hd;
    }

    public void setAvatar_hd(String avatar_hd) {
        this.avatar_hd = avatar_hd;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public int getBi_followers_count() {
        return bi_followers_count;
    }

    public void setBi_followers_count(int bi_followers_count) {
        this.bi_followers_count = bi_followers_count;
    }

    public int getBlock_app() {
        return block_app;
    }

    public void setBlock_app(int block_app) {
        this.block_app = block_app;
    }

    public int getBlock_word() {
        return block_word;
    }

    public void setBlock_word(int block_word) {
        this.block_word = block_word;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getClassX() {
        return classX;
    }

    public void setClassX(int classX) {
        this.classX = classX;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getCredit_score() {
        return credit_score;
    }

    public void setCredit_score(int credit_score) {
        this.credit_score = credit_score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public boolean isFollow_me() {
        return follow_me;
    }

    public void setFollow_me(boolean follow_me) {
        this.follow_me = follow_me;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMbrank() {
        return mbrank;
    }

    public void setMbrank(int mbrank) {
        this.mbrank = mbrank;
    }

    public int getMbtype() {
        return mbtype;
    }

    public void setMbtype(int mbtype) {
        this.mbtype = mbtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOnline_status() {
        return online_status;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public int getPagefriends_count() {
        return pagefriends_count;
    }

    public void setPagefriends_count(int pagefriends_count) {
        this.pagefriends_count = pagefriends_count;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getPtype() {
        return ptype;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public int getUrank() {
        return urank;
    }

    public void setUrank(int urank) {
        this.urank = urank;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUser_ability() {
        return user_ability;
    }

    public void setUser_ability(int user_ability) {
        this.user_ability = user_ability;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public String getVerified_reason_url() {
        return verified_reason_url;
    }

    public void setVerified_reason_url(String verified_reason_url) {
        this.verified_reason_url = verified_reason_url;
    }

    public String getVerified_source() {
        return verified_source;
    }

    public void setVerified_source(String verified_source) {
        this.verified_source = verified_source;
    }

    public String getVerified_source_url() {
        return verified_source_url;
    }

    public void setVerified_source_url(String verified_source_url) {
        this.verified_source_url = verified_source_url;
    }

    public String getVerified_trade() {
        return verified_trade;
    }

    public void setVerified_trade(String verified_trade) {
        this.verified_trade = verified_trade;
    }

    public int getVerified_type() {
        return verified_type;
    }

    public void setVerified_type(int verified_type) {
        this.verified_type = verified_type;
    }

    public String getWeihao() {
        return weihao;
    }

    public void setWeihao(String weihao) {
        this.weihao = weihao;
    }

    public static class StatusBean {
        private int attitudes_count;
        private int biz_feature;
        private int comments_count;
        private String created_at;
        private boolean favorited;
        private long id;
        private String idstr;
        private String in_reply_to_screen_name;
        private String in_reply_to_status_id;
        private String in_reply_to_user_id;
        private boolean isLongText;
        private String mid;
        private int mlevel;
        private int page_type;
        private int reposts_count;
        private String source;
        private int source_allowclick;
        private int source_type;
        private String text;
        private int textLength;
        private boolean truncated;
        private int userType;
        /**
         * list_id : 0
         * type : 0
         */

        private VisibleBean visible;
        private List<?> darwin_tags;
        private List<?> hot_weibo_tags;
        private List<?> pic_urls;
        private List<?> text_tag_tips;

        public int getAttitudes_count() {
            return attitudes_count;
        }

        public void setAttitudes_count(int attitudes_count) {
            this.attitudes_count = attitudes_count;
        }

        public int getBiz_feature() {
            return biz_feature;
        }

        public void setBiz_feature(int biz_feature) {
            this.biz_feature = biz_feature;
        }

        public int getComments_count() {
            return comments_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public boolean isFavorited() {
            return favorited;
        }

        public void setFavorited(boolean favorited) {
            this.favorited = favorited;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getIdstr() {
            return idstr;
        }

        public void setIdstr(String idstr) {
            this.idstr = idstr;
        }

        public String getIn_reply_to_screen_name() {
            return in_reply_to_screen_name;
        }

        public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
            this.in_reply_to_screen_name = in_reply_to_screen_name;
        }

        public String getIn_reply_to_status_id() {
            return in_reply_to_status_id;
        }

        public void setIn_reply_to_status_id(String in_reply_to_status_id) {
            this.in_reply_to_status_id = in_reply_to_status_id;
        }

        public String getIn_reply_to_user_id() {
            return in_reply_to_user_id;
        }

        public void setIn_reply_to_user_id(String in_reply_to_user_id) {
            this.in_reply_to_user_id = in_reply_to_user_id;
        }

        public boolean isIsLongText() {
            return isLongText;
        }

        public void setIsLongText(boolean isLongText) {
            this.isLongText = isLongText;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public int getMlevel() {
            return mlevel;
        }

        public void setMlevel(int mlevel) {
            this.mlevel = mlevel;
        }

        public int getPage_type() {
            return page_type;
        }

        public void setPage_type(int page_type) {
            this.page_type = page_type;
        }

        public int getReposts_count() {
            return reposts_count;
        }

        public void setReposts_count(int reposts_count) {
            this.reposts_count = reposts_count;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getSource_allowclick() {
            return source_allowclick;
        }

        public void setSource_allowclick(int source_allowclick) {
            this.source_allowclick = source_allowclick;
        }

        public int getSource_type() {
            return source_type;
        }

        public void setSource_type(int source_type) {
            this.source_type = source_type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getTextLength() {
            return textLength;
        }

        public void setTextLength(int textLength) {
            this.textLength = textLength;
        }

        public boolean isTruncated() {
            return truncated;
        }

        public void setTruncated(boolean truncated) {
            this.truncated = truncated;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public VisibleBean getVisible() {
            return visible;
        }

        public void setVisible(VisibleBean visible) {
            this.visible = visible;
        }

        public List<?> getDarwin_tags() {
            return darwin_tags;
        }

        public void setDarwin_tags(List<?> darwin_tags) {
            this.darwin_tags = darwin_tags;
        }

        public List<?> getHot_weibo_tags() {
            return hot_weibo_tags;
        }

        public void setHot_weibo_tags(List<?> hot_weibo_tags) {
            this.hot_weibo_tags = hot_weibo_tags;
        }

        public List<?> getPic_urls() {
            return pic_urls;
        }

        public void setPic_urls(List<?> pic_urls) {
            this.pic_urls = pic_urls;
        }

        public List<?> getText_tag_tips() {
            return text_tag_tips;
        }

        public void setText_tag_tips(List<?> text_tag_tips) {
            this.text_tag_tips = text_tag_tips;
        }

        public static class VisibleBean {
            private int list_id;
            private int type;

            public int getList_id() {
                return list_id;
            }

            public void setList_id(int list_id) {
                this.list_id = list_id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }


    @Override
    public String toString() {
        return "WeiBoBean{" +
                "allow_all_act_msg=" + allow_all_act_msg +
                ", allow_all_comment=" + allow_all_comment +
                ", avatar_hd='" + avatar_hd + '\'' +
                ", avatar_large='" + avatar_large + '\'' +
                ", bi_followers_count=" + bi_followers_count +
                ", block_app=" + block_app +
                ", block_word=" + block_word +
                ", city='" + city + '\'' +
                ", classX=" + classX +
                ", created_at='" + created_at + '\'' +
                ", credit_score=" + credit_score +
                ", description='" + description + '\'' +
                ", domain='" + domain + '\'' +
                ", favourites_count=" + favourites_count +
                ", follow_me=" + follow_me +
                ", followers_count=" + followers_count +
                ", following=" + following +
                ", friends_count=" + friends_count +
                ", gender='" + gender + '\'' +
                ", geo_enabled=" + geo_enabled +
                ", id=" + id +
                ", idstr='" + idstr + '\'' +
                ", lang='" + lang + '\'' +
                ", location='" + location + '\'' +
                ", mbrank=" + mbrank +
                ", mbtype=" + mbtype +
                ", name='" + name + '\'' +
                ", online_status=" + online_status +
                ", pagefriends_count=" + pagefriends_count +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", profile_url='" + profile_url + '\'' +
                ", province='" + province + '\'' +
                ", ptype=" + ptype +
                ", remark='" + remark + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", star=" + star +
                ", status=" + status +
                ", statuses_count=" + statuses_count +
                ", urank=" + urank +
                ", url='" + url + '\'' +
                ", user_ability=" + user_ability +
                ", verified=" + verified +
                ", verified_reason='" + verified_reason + '\'' +
                ", verified_reason_url='" + verified_reason_url + '\'' +
                ", verified_source='" + verified_source + '\'' +
                ", source='" + source + '\'' +
                ", verified_source_url='" + verified_source_url + '\'' +
                ", verified_trade='" + verified_trade + '\'' +
                ", verified_type=" + verified_type +
                ", weihao='" + weihao + '\'' +
                '}';
    }
}
