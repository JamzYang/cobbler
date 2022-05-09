package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 3:07 上午
 */
public class ListParserTest {
    @Test
    public void testParseList(){
        String result = "- 有人认为，设计就是讨论要用什么技术实现功能；\n" +
                "- 有人认为，设计就是设计模式；\n\n";
        String testString ="<div class=\"se-4ea65bf9\" data-slate-type=\"list\" data-slate-object=\"block\" data-key=\"650\">\n" +
                "    <div class=\"se-c63d9e19 se-033633fa\" data-slate-type=\"list-line\" data-slate-object=\"block\" data-key=\"651\">\n" +
                "        <span data-slate-object=\"text\" data-key=\"652\">\n" +
                "            <span data-slate-leaf=\"true\" data-offset-key=\"652:0\" data-first-offset=\"true\">\n" +
                "                <span data-slate-string=\"true\">有人认为，设计就是讨论要用什么技术实现功能；</span>\n" +
                "            </span>\n" +
                "        </span>\n" +
                "    </div>\n" +
                "    <div class=\"se-96e5ed12 se-d8303bf1\" data-slate-type=\"list-line\" data-slate-object=\"block\" data-key=\"655\">\n" +
                "        <span data-slate-object=\"text\" data-key=\"656\">\n" +
                "            <span data-slate-leaf=\"true\" data-offset-key=\"656:0\" data-first-offset=\"true\">\n" +
                "                <span data-slate-string=\"true\">有人认为，设计就是设计模式；</span>\n" +
                "            </span>\n" +
                "        </span>\n" +
                "    </div>\n" +
                "</div>";

        Document document = Jsoup.parseBodyFragment(testString);
        Element body = document.body();
        Elements children = body.children();
        BlockParser listParser = new ListParser();
        JSONObject parse = listParser.parse(children.get(0));

        parse.getString("content");
        assertEquals("无序列表内容解析错误",result,parse.getString("content"));
    }
}
