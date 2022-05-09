package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageParserTest {
    @Test
    public void testParseImage(){
        String result = "![alt](https://static001.geekbang.org/resource/image/a6/20/a6920a1e9a4a8af9fe86b88f032cb820.jpg)";
        String body = "<div class=\"se-b15fdca9\" data-slate-type=\"image\" data-slate-object=\"block\" data-key=\"686\">\n" +
                "    <img class=\"se-582d127d\" data-savepage-src=\"https://static001.geekbang.org/resource/image/a6/20/a6920a1e9a4a8af9fe86b88f032cb820.jpg\" src=\"\">\n" +
                "</div>";
        ImageParser imageParser = new ImageParser();
        Document document = Jsoup.parseBodyFragment(body);
        Element body1 = document.body();

        JSONObject parse = imageParser.parse(body1);
        parse.getString("content");
        assertEquals("图片内容解析错误",parse.getString("content"),result);
        System.out.println();

    }
}