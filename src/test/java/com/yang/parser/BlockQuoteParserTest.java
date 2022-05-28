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
 * @date 2022/5/10 12:40 上午
 */
public class BlockQuoteParserTest {

    @Test
    public void testParseQuote(){
        String result = "> 设计是为了让软件在长期更容易适应变化。\n>\n";
        String body = "<div class=\"se-f2dc9f09\" data-slate-type=\"block-quote\" data-slate-object=\"block\" data-key=\"187\"><div class=\"se-d926b6f9\" data-slate-type=\"quote-line\" data-slate-object=\"block\" data-key=\"188\"><span data-slate-object=\"text\" data-key=\"189\"><span data-slate-leaf=\"true\" data-offset-key=\"189:0\" data-first-offset=\"true\"><span data-slate-string=\"true\">设计是为了让软件在长期更容易适应变化。</span></span></span></div></div>";
        Document document = Jsoup.parseBodyFragment(body);
        Element body1 = document.body();
        BlockParser parser = BlockParser.createBlockParser(body1.child(0));
        JSONObject parse = parser.parse();
        parse.getString("content");
        assertEquals("引用解析错误",result,parse.getString("content"));
        System.out.println();

    }

    @Test
    public void testParseQuoteAndParagraph(){
        String result = "> Design is there to enable you to keep changing the software easily in the long term.\n" +
                ">\n" +
                "> —— Kent Beck\n" +
                "> \n" +
                "\n" +
                "软件设计，是一门关注长期变化的学问。它并不是程序员的入门第一课，因为初窥编程门径的程序员首先追求的是把一个功能实现出来，他无法看到一个软件长期的变化。\n";

        String body = "<div class=\"se-7b4ddff1\" data-slate-type=\"block-quote\" data-slate-object=\"block\" data-key=\"190\"><div class=\"se-d926b6f9\" data-slate-type=\"quote-line\" data-slate-object=\"block\" data-key=\"191\"><span data-slate-object=\"text\" data-key=\"192\"><span data-slate-leaf=\"true\" data-offset-key=\"192:0\" data-first-offset=\"true\"><span data-slate-string=\"true\">Design is there to enable you to keep changing the software easily in the long term.</span></span></span></div></div>" +
                "<div class=\"se-c907fba1\" data-slate-type=\"block-quote\" data-slate-object=\"block\" data-key=\"193\"><div class=\"se-d926b6f9\" data-slate-type=\"quote-line\" data-slate-object=\"block\" data-key=\"194\"><span data-slate-object=\"text\" data-key=\"195\"><span data-slate-leaf=\"true\" data-offset-key=\"195:0\" data-first-offset=\"true\"><span data-slate-string=\"true\">—— Kent Beck</span></span></span></div></div>" +
                "<div class=\"se-d6cee44d \" data-slate-type=\"paragraph\" data-slate-object=\"block\" data-key=\"196\"><span data-slate-object=\"text\" data-key=\"197\"><span data-slate-leaf=\"true\" data-offset-key=\"197:0\" data-first-offset=\"true\"><span data-slate-string=\"true\">软件设计，是一门关注长期变化的学问。它并不是程序员的入门第一课，因为初窥编程门径的程序员首先追求的是把一个功能实现出来，他无法看到一个软件长期的变化。</span></span></span></div> ";
        Document document = Jsoup.parseBodyFragment(body);
        Element body1 = document.body();
        Elements children = body1.children();
        StringBuilder stringBuilder = new StringBuilder();
        for (Element child : children) {
            BlockParser blockParser = BlockParser.createBlockParser(child);
            stringBuilder.append(blockParser.parse().getString("content"));
        }
        assertEquals("引用解析错误",result,stringBuilder.toString());
    }

}
