package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.EnumAttr;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.List;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:34 上午
 */
public abstract class TextParser implements Parser{

    public final String parseText(Element element){
       return parse(element).getString("content");
    }

}
