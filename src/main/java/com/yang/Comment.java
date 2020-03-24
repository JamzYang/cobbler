package com.yang;

import java.util.List;

/**
 * @author yang
 * Date 2020/3/24 23:01
 */
public class Comment {
    private String had_liked;
    private String product_id;
    private String comment_is_top;
    private String id;
    private String user_header;
    private String comment_ctime;
    private String can_delete;
    private String user_name;
    private String product_type;
    private List<Replay> replies;

    public Comment() {
    }

    public String getHad_liked() {
        return had_liked;
    }

    public void setHad_liked(String had_liked) {
        this.had_liked = had_liked;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getComment_is_top() {
        return comment_is_top;
    }

    public void setComment_is_top(String comment_is_top) {
        this.comment_is_top = comment_is_top;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_header() {
        return user_header;
    }

    public void setUser_header(String user_header) {
        this.user_header = user_header;
    }

    public String getComment_ctime() {
        return comment_ctime;
    }

    public void setComment_ctime(String comment_ctime) {
        this.comment_ctime = comment_ctime;
    }

    public String getCan_delete() {
        return can_delete;
    }

    public void setCan_delete(String can_delete) {
        this.can_delete = can_delete;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public List<Replay> getReplies() {
        return replies;
    }

    public void setReplies(List<Replay> replies) {
        this.replies = replies;
    }
}
