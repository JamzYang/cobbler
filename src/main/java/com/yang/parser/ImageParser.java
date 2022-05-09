package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.EnumAttr;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:35 上午
 */
@NoArgsConstructor
public class ImageParser extends BlockParser{
    public ImageParser(Element blockElement) {
        super(blockElement);
    }

    @Override
    public JSONObject parse() {
        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
       contentBuilder.append("![alt](");
        Elements img = getBlockElement().select("img");
        if(img.hasAttr(EnumAttr.IMAGE_SRC.getAttr())){
            String src = img.attr(EnumAttr.IMAGE_SRC.getAttr());
            contentBuilder.append(src).append(")").append("\n");
            jsonObject.put("content",contentBuilder.toString());
        }
        return jsonObject;
    }
}
