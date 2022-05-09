package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.SelectUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Optional;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:34 上午
 */
public class ListParser extends BlockParser{
    @Override
    public JSONObject parse(Element element) {

        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        Elements element1 = element.select(SelectUtil.LIST_LINE);
        for (Element element2 : element1) {
            contentBuilder.append("- ");
            Elements elements2 = element2.select(SelectUtil.OBJECT_TEXT);
            for (Element element3 : elements2) {
                ObjectTextParser objectTextParser = new ObjectTextParser();
                String text = objectTextParser.parseText(element3);
                contentBuilder.append(text).append("\n");
            }
        }
        contentBuilder.append("\n");
        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
