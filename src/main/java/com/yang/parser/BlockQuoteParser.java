package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.SelectUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/10 1:34 上午
 */
public class BlockQuoteParser extends BlockParser {
    public BlockQuoteParser(Element blockElement) {
        super(blockElement);
    }

    @Override
    public JSONObject parse() {
        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("> ");
        Elements quoteElements = getBlockElement().select(SelectUtil.BLOCK_QUOTE);
        if(quoteElements.size() != 1) {
            throw new RuntimeException("引用元素数量异常");
        }
        Elements quoteLineElements = quoteElements.get(0).children();
        for (Element quoteLineElement : quoteLineElements) {
            Elements stringElements = quoteLineElement.select("[data-slate-string=true]");
            for (Element stringElement : stringElements) {
                if(stringElement.childNodeSize() > 1){
                    throw new RuntimeException("引用行 数量异常");
                }
                Node node = stringElement.childNode(0);
                contentBuilder.append(((TextNode)node).getWholeText()).append("\n");
            }
            contentBuilder.append(">").append("\n");
        }
        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
