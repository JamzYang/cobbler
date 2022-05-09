package com.yang;

import lombok.Data;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 5:37 下午
 */
@Data
public class Article {
    private String articleUrl;
    private String articleId;

    private List<Comment> commentList;
    private StringBuilder content;
    private StringBuilder comments;

    private Document doc;
}
