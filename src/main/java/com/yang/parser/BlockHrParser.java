package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.SelectUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/10 1:58 上午
 */
public class BlockHrParser extends BlockParser {
    public BlockHrParser(Element blockElement) {
        super(blockElement);
    }

    @Override
    public JSONObject parse() {
        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("***");
        Elements hrElements = getBlockElement().select(SelectUtil.BLOCK_HR);
        if(hrElements.size() != 1) {
            throw new RuntimeException("引用元素数量异常");
        }
        contentBuilder.append("\n");
        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
