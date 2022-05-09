package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImageParserTest {
    @Test
    public void testParseImage(){
        String result = "![alt](https://static001.geekbang.org/resource/image/a6/20/a6920a1e9a4a8af9fe86b88f032cb820.jpg)\n";
        String body = "<div class=\"se-b15fdca9\" data-slate-type=\"image\" data-slate-object=\"block\" data-key=\"686\">\n" +
                "    <img class=\"se-582d127d\" data-savepage-src=\"https://static001.geekbang.org/resource/image/a6/20/a6920a1e9a4a8af9fe86b88f032cb820.jpg\" src=\"\">\n" +
                "</div>";
        Document document = Jsoup.parseBodyFragment(body);
        Element body1 = document.body();
        ImageParser imageParser = new ImageParser(body1);

        JSONObject parse = imageParser.parse();
        parse.getString("content");
        assertEquals("图片内容解析错误",parse.getString("content"),result);
        System.out.println();

    }
}