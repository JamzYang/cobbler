package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.SelectUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:34 上午
 */
public class ParagraphParser extends BlockParser{
    private static String dataSlateString = "data-slate-string";
    private static String dataSlateObject = "data-slate-object";
    private static String dataSlateLeaf = "data-slate-leaf";
    @Override
    public JSONObject parse(Element element) {

        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        Elements select = element.select(SelectUtil.OBJECT_TEXT);
        for (Element element1 : select) {
            ObjectTextParser objectTextParser = new ObjectTextParser();
            String text = objectTextParser.parseText(element1);
            contentBuilder.append(text);
        }


        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
