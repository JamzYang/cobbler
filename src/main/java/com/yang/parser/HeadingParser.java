package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.EnumAttr;
import com.yang.SelectUtil;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:35 上午
 */
@NoArgsConstructor
public class HeadingParser extends BlockParser{

    public HeadingParser(Element blockElement) {
        super(blockElement);
    }

    @Override
    public JSONObject parse() {
        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("### ");
        Elements h2 = getBlockElement().select("h2");
        if(h2.hasAttr(EnumAttr.HEADING.getAttr())){

            Elements select = h2.select(SelectUtil.OBJECT_TEXT);
            for (Element element1 : select) {
                ObjectTextParser objectTextParser = new ObjectTextParser(element1);
                String text = objectTextParser.parseText();
                contentBuilder.append(text);
            }

        }
        contentBuilder.append("\n");
        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
