package com.yang;

import lombok.Data;
import org.jsoup.nodes.Document;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 5:37 下午
 */
@Data
public class Article {
    private String articleUrl;
    private String articleId;
    private StringBuilder content;
    private StringBuilder comments;

    private Document doc;
}
