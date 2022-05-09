package com.yang;

import com.alibaba.fastjson.JSONObject;
import com.yang.parser.BlockParser;
import com.yang.parser.CommentsBuilder;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 5:37 下午
 */
@Data
public class Article {
    private String title;
    private String articleUrl;
    private String articleId;

    private List<Comment> commentList;
    private StringBuilder content;
    private StringBuilder comments;

    private Document doc;


    public void build() {
        buildContent();
        buildComments();
    }

    private void buildComments() {
        CommentsBuilder commentsBuilder = new CommentsBuilder(this); //todo
        commentsBuilder.build();
    }

    private void buildContent() {
        Element app = doc.body().getElementById("app");
        Elements contentsElements = app.select("[data-slate-editor=true], [autocorrect=off]");
        if (contentsElements.size() != 1) {
            throw new RuntimeException("正文未识别");
        }
        List<JSONObject> list = new ArrayList<>();
        Element contentElement = contentsElements.get(0);
        Elements blockElements = contentElement.children();
        for (Element blockElement : blockElements) {
            BlockParser blockParser = BlockParser.createBlockParser(blockElement);
            list.add(blockParser.parse());
        }
        StringBuilder contentSB = new StringBuilder();
        list.forEach(item -> contentSB.append(item.getString("content")));
        this.content = contentSB;
    }
}
