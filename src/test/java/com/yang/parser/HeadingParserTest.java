package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeadingParserTest {
    @Test
    public void testParseHeading(){
        String result = "### 核心的模型\n";
        String body = "<h2 class=\"se-7b8aeef3\" data-slate-type=\"heading\" data-slate-object=\"block\" data-key=\"678\">\n" +
                "    <span data-slate-object=\"text\" data-key=\"679\">\n" +
                "        <span data-slate-leaf=\"true\" data-offset-key=\"679:0\" data-first-offset=\"true\">\n" +
                "            <span data-slate-string=\"true\">核心的模型</span>\n" +
                "        </span>\n" +
                "    </span>\n" +
                "</h2>";
        BlockParser parser = new HeadingParser();
        Document document = Jsoup.parseBodyFragment(body);
        Element body1 = document.body();

        JSONObject parse = parser.parse(body1);
        parse.getString("content");
        assertEquals("二级标题解析错误",result,parse.getString("content"));
        System.out.println();

    }
}