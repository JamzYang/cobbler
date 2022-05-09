package com.yang;

import lombok.Data;

import java.util.List;

/**
 * @author yang
 * Date 2020/3/24 23:01
 */
@Data
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
    private String uid;
    private String discussion_count;
    private String ucode;
    private String score;
    private String comment_content;
    private String like_count;
    private List<Replay> replies;

}
