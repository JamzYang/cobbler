package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
}
