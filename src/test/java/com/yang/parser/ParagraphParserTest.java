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
public class ParagraphParserTest {
    @Test
    public void testParseParagraph(){
        String result = "你好！我是郑晔。\n";
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
        assertEquals("段落内容解析错误",result,parse.getString("content"));
    }


    @Test
    public void testParseParagraphHasBoldText(){
        String testString = "<div class=\"se-fd518b29 \" data-slate-type=\"paragraph\" data-slate-object=\"block\" data-key=\"689\">\n" +
                "    <span data-slate-object=\"text\" data-key=\"690\">\n" +
                "        <span data-slate-leaf=\"true\" data-offset-key=\"690:0\" data-first-offset=\"true\">\n" +
                "            <span data-slate-string=\"true\">而在软件的开发过程中，这种统一的结构就是模型，而</span>\n" +
                "        </span>\n" +
                "    </span>\n" +
                "    <span data-slate-object=\"text\" data-key=\"691\">\n" +
                "        <span data-slate-leaf=\"true\" data-offset-key=\"691:0\" data-first-offset=\"true\">\n" +
                "            <span class=\"se-62c91266\" data-slate-type=\"bold\" data-slate-object=\"mark\">\n" +
                "                <span data-slate-string=\"true\">软件设计就是要构建出一套模型</span>\n" +
                "            </span>\n" +
                "        </span>\n" +
                "    </span>\n" +
                "    <span data-slate-object=\"text\" data-key=\"692\">\n" +
                "        <span data-slate-leaf=\"true\" data-offset-key=\"692:0\" data-first-offset=\"true\">\n" +
                "            <span data-slate-string=\"true\">。</span>\n" +
                "        </span>\n" +
                "    </span>\n" +
                "</div>";

        String result = "而在软件的开发过程中，这种统一的结构就是模型，而**软件设计就是要构建出一套模型**。\n";

        Document document = Jsoup.parseBodyFragment(testString);
        Element body = document.body();
        Elements children = body.children();
        ParagraphParser paragraphParser = new ParagraphParser();
        JSONObject parse = paragraphParser.parse(children.get(0));

        parse.getString("content");
        assertEquals("段落内容解析错误",result,parse.getString("content"));
    }

    @Test
    public void testDoubleStar(){
        String testString = "<div class=\"se-94ab027c \" data-slate-type=\"paragraph\" data-slate-object=\"block\" data-key=\"713\"><span data-slate-object=\"text\" data-key=\"714\"><span data-slate-leaf=\"true\" data-offset-key=\"714:0\" data-first-offset=\"true\"><span data-slate-string=\"true\">即便是在一个软件内部，模型也可以是分层的。</span></span></span><span data-slate-object=\"text\" data-key=\"715\"><span data-slate-leaf=\"true\" data-offset-key=\"715:0\" data-first-offset=\"true\"><span class=\"se-9de3dbdb\" data-slate-type=\"bold\" data-slate-object=\"mark\"><span data-slate-string=\"true\">我们可以先从最核心的模型</span></span></span></span><span data-slate-object=\"text\" data-key=\"716\"><span data-slate-leaf=\"true\" data-offset-key=\"716:0\" data-first-offset=\"true\"><span class=\"se-dc7ae141\" data-slate-type=\"bold\" data-slate-object=\"mark\"><span data-slate-string=\"true\">开始构建</span></span></span></span><span data-slate-object=\"text\" data-key=\"717\"><span data-slate-leaf=\"true\" data-offset-key=\"717:0\" data-first-offset=\"true\"><span class=\"se-67384397\" data-slate-type=\"bold\" data-slate-object=\"mark\"><span data-slate-string=\"true\">，有了这个核心模型之后，</span></span></span></span><span data-slate-object=\"text\" data-key=\"718\"><span data-slate-leaf=\"true\" data-offset-key=\"718:0\" data-first-offset=\"true\"><span class=\"se-a0ca5c01\" data-slate-type=\"bold\" data-slate-object=\"mark\"><span data-slate-string=\"true\">可以</span></span></span></span><span data-slate-object=\"text\" data-key=\"719\"><span data-slate-leaf=\"true\" data-offset-key=\"719:0\" data-first-offset=\"true\"><span class=\"se-a3203a0c\" data-slate-type=\"bold\" data-slate-object=\"mark\"><span data-slate-string=\"true\">通过组合这些基础的模型，构建出上面一层的模型</span></span></span></span><span data-slate-object=\"text\" data-key=\"720\"><span data-slate-leaf=\"true\" data-offset-key=\"720:0\" data-first-offset=\"true\"><span data-slate-string=\"true\">。</span></span></span></div>";
        String result = "即便是在一个软件内部，模型也可以是分层的。**我们可以先从最核心的模型开始构建，有了这个核心模型之后，可以通过组合这些基础的模型，构建出上面一层的模型**。\n";

        Document document = Jsoup.parseBodyFragment(testString);
        Element body = document.body();
        Elements children = body.children();
        ParagraphParser paragraphParser = new ParagraphParser();
        JSONObject parse = paragraphParser.parse(children.get(0));

        parse.getString("content");
        assertEquals("段落内容解析错误",result,parse.getString("content"));

    }
}
