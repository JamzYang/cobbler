package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.EnumAttr;
import lombok.Setter;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:35 上午
 */
@Setter
public class HeadingParser extends BlockParser{
//    private ObjectTextParser objectTextParser;

    private static String dataSavepageSrc = "data-savepage-src";
    @Override
    public JSONObject parse(Element element) {
        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("### ");
        Elements h2 = element.select("h2");
        if(h2.hasAttr(EnumAttr.HEADING.getAttr())){

            Elements select = h2.select(String.format("[%s=%s]", EnumAttr.OBJECT_TEXT.getAttr(), EnumAttr.OBJECT_TEXT.getValue()));
            for (Element element1 : select) {
                ObjectTextParser objectTextParser = new ObjectTextParser();
                String text = objectTextParser.parseText(element1);
                contentBuilder.append(text);
            }

        }
        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
