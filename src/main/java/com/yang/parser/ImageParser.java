package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.EnumAttr;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:35 上午
 */
public class ImageParser extends BlockParser{
    private static String dataSavepageSrc = "data-savepage-src";
    @Override
    public JSONObject parse(Element element) {
        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
       contentBuilder.append("![alt](");
        Elements img = element.select("img");
        if(img.hasAttr(EnumAttr.IMAGE_SRC.getAttr())){
            String src = img.attr(EnumAttr.IMAGE_SRC.getAttr());
            contentBuilder.append(src).append(")");
            jsonObject.put("content",contentBuilder.toString());
        }
        return jsonObject;
    }
}
