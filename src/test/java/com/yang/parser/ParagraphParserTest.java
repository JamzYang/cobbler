package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 3:07 上午
 */
public class ParagraphParserTest {
    @Test
    public void testParseParagraph(){
        String result = "你好！我是郑晔。";
        String testString = "<div class=\"se-3745f9ed \" data-slate-type=\"paragraph\" data-slate-object=\"block\" data-key=\"1611\">\n" +
                "    <span data-slate-object=\"text\" data-key=\"1612\">\n" +
                "        <span data-slate-leaf=\"true\" data-offset-key=\"1612:0\" data-first-offset=\"true\">\n" +
                "            <span data-slate-string=\"true\">你好！我是郑晔。</span>\n" +
                "        </span>\n" +
                "    </span>\n" +
                "</div>";

        Document document = Jsoup.parseBodyFragment(testString);
        Element body = document.body();
        Elements children = body.children();
        ParagraphParser paragraphParser = new ParagraphParser();
        JSONObject parse = paragraphParser.parse(children.get(0));

        parse.getString("content");
        assertEquals("段落内容解析错误",parse.getString("content"),result);
        System.out.println();
    }
}
