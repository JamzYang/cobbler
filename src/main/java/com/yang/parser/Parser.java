package com.yang.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Element;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:31 上午
 */
public interface Parser {
    JSONObject parse(Element element);
}
