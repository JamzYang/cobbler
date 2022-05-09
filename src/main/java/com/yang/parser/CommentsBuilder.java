package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.Article;
import com.yang.Comment;
import com.yang.Replay;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yang.HttpUtil.postJson;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 4:21 下午
 */
@Log4j2
public class CommentsBuilder {
    private Article article;


    public CommentsBuilder(Article article) {
        this.article = article;
    }

    public void build(){
        List<Comment> commentList = grabComments();

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
        article.setComments(commentsSB);
    }

    //post 抓取评论信息
    private List<Comment> grabComments() {
        if(CollectionUtils.isNotEmpty(article.getCommentList())){
            return article.getCommentList(); //todo
        }
        log.debug("开始抓取评论信息...");
        long start = System.currentTimeMillis();
        JSONObject jsonOb = new JSONObject();
        jsonOb.put("aid", article.getArticleId());
        jsonOb.put("prev", "0");
        List<Comment> commentList = doGetComments(jsonOb);
        System.out.println(JSONObject.toJSONString(commentList));
        long end = System.currentTimeMillis();
        log.debug("耗时: " + (end - start) / 1000.0 + "秒.");
        log.debug("评论信息已抓取: " + commentList.size() + "条.\t 耗时: " + (end - start) / 1000.0 + "秒.");
        return commentList;
    }

    private List<Comment> doGetComments(JSONObject jsonOb) {
        String jsonParam = jsonOb.toJSONString();
        String commentsUrl = "https://time.geekbang.org/serv/v1/comments";
        String respResult = postJson(commentsUrl, jsonParam, article.getArticleUrl());
        if(StringUtils.isBlank(respResult)){
            throw new RuntimeException("获取评论信息失败");
        }
        JSONObject resJson = JSONObject.parseObject(respResult);
        JSONObject data = resJson.getJSONObject("data");
        List<Comment> commentList = new ArrayList<>(data.getJSONArray("list").toJavaList(Comment.class));
        boolean hasMore = data.getJSONObject("page").getBoolean("more");
        while (hasMore) {
            jsonOb.put("prev", commentList.get(commentList.size() - 1).getScore());
            resJson = JSONObject.parseObject(respResult);
            data = resJson.getJSONObject("data");
            commentList.addAll(data.getJSONArray("list").toJavaList(Comment.class));
            hasMore = data.getJSONObject("page").getBoolean("more");
        }
        return commentList;
    }

}
