package com.yang.parser;

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
public class OjectTextParserTest {
    @Test
    public void testParseParagraph(){
        String result = "核心的模型";
        String testString = "    <span data-slate-object=\"text\" data-key=\"679\">\n" +
                "        <span data-slate-leaf=\"true\" data-offset-key=\"679:0\" data-first-offset=\"true\">\n" +
                "            <span data-slate-string=\"true\">核心的模型</span>\n" +
                "        </span>\n" +
                "    </span>";



        Document document = Jsoup.parseBodyFragment(testString);
        Element body = document.body();
        Elements children = body.children();

        ObjectTextParser textParser = new ObjectTextParser(children.get(0));
        String text = textParser.parseText();

        assertEquals("段落内容解析错误",result,text);
    }
}
