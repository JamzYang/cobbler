package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.Comment;
import com.yang.Replay;
import org.apache.commons.collections.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 4:21 下午
 */
public class CommentsBuilder {
    public StringBuilder build(List<Comment> commentList){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        StringBuilder commentsSB = new StringBuilder();
        commentsSB.append("*** \n");
        for (Comment comment : commentList) {
            commentsSB.append("**`").append(comment.getUser_name()).append("`**").append(" ")
                    .append("*").append(sdf.format(new Date(Long.parseLong(comment.getComment_ctime()) * 1000))).append("*\n")
                    .append("> ").append(comment.getComment_content().replaceAll("<br>","")).append("\n");
            if(CollectionUtils.isNotEmpty(comment.getReplies())){
                List<Replay> replies = comment.getReplies();
                for (Replay reply : replies) {
                    commentsSB.append("> > `").append(reply.getUser_name()).append("`:").append(reply.getContent()).append("\n");
                }
            }
            String likedStr = String.format("<p align=\"right\">\uD83D\uDC4D : %s</p>", comment.getLike_count());
            commentsSB.append(likedStr).append("\n").append("***").append("\n");
        }

        return commentsSB;
    }
}
