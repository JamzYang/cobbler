package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.SelectUtil;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:34 上午
 */
@NoArgsConstructor
public class ListParser extends BlockParser{
    public ListParser(Element blockElement) {
        super(blockElement);
    }

    @Override
    public JSONObject parse() {

        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        Elements element1 = getBlockElement().select(SelectUtil.LIST_LINE);
        for (Element element2 : element1) {
            contentBuilder.append("- ");
            Elements elements2 = element2.select(SelectUtil.OBJECT_TEXT);
            for (Element element3 : elements2) {
                ObjectTextParser objectTextParser = new ObjectTextParser(element3);
                String text = objectTextParser.parseText();
                contentBuilder.append(text).append("\n");
            }
        }
        contentBuilder.append("\n");
        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
