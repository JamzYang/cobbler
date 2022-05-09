package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.EnumAttr;
import com.yang.SelectUtil;
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

            Elements select = h2.select(SelectUtil.OBJECT_TEXT);
            for (Element element1 : select) {
                ObjectTextParser objectTextParser = new ObjectTextParser();
                String text = objectTextParser.parseText(element1);
                contentBuilder.append(text);
            }

        }
        contentBuilder.append("\n");
        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
