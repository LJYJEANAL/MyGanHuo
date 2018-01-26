package com.ng.ganhuoapi.data.video;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */

public class CommentData {
    private long id;
    private String text;
    private String content_rich_span;

    private int reply_count;
    private List<replyData> reply_list;
    /**
     * 点赞
     */
    private int digg_count;
    private int bury_count;
    private long create_time;
    private long user_id;
    private String user_name;
    private String remark_name;
    private String user_profile_image_url;
    private boolean user_verified;
    private int is_following;
    private int is_followed;
    private int is_pgc_author;
    private int is_blocking;
    private int is_blocked;
    private String verified_reason;
    private int user_bury;
    private int user_digg;
    private int user_relation;
    private String user_auth_info;
    private String platform;
    private MediaInfoBean media_info;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContent_rich_span() {
        return content_rich_span;
    }

    public void setContent_rich_span(String content_rich_span) {
        this.content_rich_span = content_rich_span;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public List<replyData> getReply_list() {
        return reply_list;
    }

    public void setReply_list(List<replyData> reply_list) {
        this.reply_list = reply_list;
    }

    public int getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(int digg_count) {
        this.digg_count = digg_count;
    }

    public int getBury_count() {
        return bury_count;
    }

    public void setBury_count(int bury_count) {
        this.bury_count = bury_count;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRemark_name() {
        return remark_name;
    }

    public void setRemark_name(String remark_name) {
        this.remark_name = remark_name;
    }

    public String getUser_profile_image_url() {
        return user_profile_image_url;
    }

    public void setUser_profile_image_url(String user_profile_image_url) {
        this.user_profile_image_url = user_profile_image_url;
    }

    public boolean isUser_verified() {
        return user_verified;
    }

    public void setUser_verified(boolean user_verified) {
        this.user_verified = user_verified;
    }

    public int getIs_following() {
        return is_following;
    }

    public void setIs_following(int is_following) {
        this.is_following = is_following;
    }

    public int getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(int is_followed) {
        this.is_followed = is_followed;
    }

    public int getIs_pgc_author() {
        return is_pgc_author;
    }

    public void setIs_pgc_author(int is_pgc_author) {
        this.is_pgc_author = is_pgc_author;
    }

    public int getIs_blocking() {
        return is_blocking;
    }

    public void setIs_blocking(int is_blocking) {
        this.is_blocking = is_blocking;
    }

    public int getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(int is_blocked) {
        this.is_blocked = is_blocked;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public int getUser_bury() {
        return user_bury;
    }

    public void setUser_bury(int user_bury) {
        this.user_bury = user_bury;
    }

    public int getUser_digg() {
        return user_digg;
    }

    public void setUser_digg(int user_digg) {
        this.user_digg = user_digg;
    }

    public int getUser_relation() {
        return user_relation;
    }

    public void setUser_relation(int user_relation) {
        this.user_relation = user_relation;
    }

    public String getUser_auth_info() {
        return user_auth_info;
    }

    public void setUser_auth_info(String user_auth_info) {
        this.user_auth_info = user_auth_info;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public MediaInfoBean getMedia_info() {
        return media_info;
    }

    public void setMedia_info(MediaInfoBean media_info) {
        this.media_info = media_info;
    }

    public static class replyData {
        private long id;
        private String text;
        private long user_id;
        private String user_name;
        private boolean user_verified;
        private String user_auth_info;
        private String user_profile_image_url;

        private int is_pgc_author;
        private List<authorButhorBean> author_badge;
        private int digg_count;
        private int user_digg;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public boolean isUser_verified() {
            return user_verified;
        }

        public void setUser_verified(boolean user_verified) {
            this.user_verified = user_verified;
        }

        public String getUser_auth_info() {
            return user_auth_info;
        }

        public void setUser_auth_info(String user_auth_info) {
            this.user_auth_info = user_auth_info;
        }

        public String getUser_profile_image_url() {
            return user_profile_image_url;
        }

        public void setUser_profile_image_url(String user_profile_image_url) {
            this.user_profile_image_url = user_profile_image_url;
        }

        public int getIs_pgc_author() {
            return is_pgc_author;
        }

        public void setIs_pgc_author(int is_pgc_author) {
            this.is_pgc_author = is_pgc_author;
        }

        public List<authorButhorBean> getAuthor_badge() {
            return author_badge;
        }

        public void setAuthor_badge(List<authorButhorBean> author_badge) {
            this.author_badge = author_badge;
        }

        public int getDigg_count() {
            return digg_count;
        }

        public void setDigg_count(int digg_count) {
            this.digg_count = digg_count;
        }

        public int getUser_digg() {
            return user_digg;
        }

        public void setUser_digg(int user_digg) {
            this.user_digg = user_digg;
        }
    }

    public static class authorButhorBean {
        private String url;
        private String open_url;
        private int width;
        private int height;
        private List<UrlListBean> url_list;
        private String uri;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getOpen_url() {
            return open_url;
        }

        public void setOpen_url(String open_url) {
            this.open_url = open_url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<UrlListBean> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<UrlListBean> url_list) {
            this.url_list = url_list;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public static class UrlListBean {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class MediaInfoBean {
        private String name;
        private String avatar_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }
}
