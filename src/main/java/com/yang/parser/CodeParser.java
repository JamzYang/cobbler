package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.EnumAttr;
import com.yang.SelectUtil;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:35 上午
 */
@NoArgsConstructor
public class CodeParser extends BlockParser{

    public CodeParser(Element blockElement) {
        super(blockElement);
    }

    @Override
    public JSONObject parse() {
        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        String attr = getBlockElement().attr(EnumAttr.CODE_LANGUAGE.getAttr());
        contentBuilder.append("```").append(getBlockElement().attr(EnumAttr.CODE_LANGUAGE.getAttr())).append("\n");
        Elements previewElements = getBlockElement().select(SelectUtil.CODE_PREVIEW);
        Elements codeLineElements;
        if(previewElements.size() != 1) {
            codeLineElements = getBlockElement().select(SelectUtil.CODE_LINE);
            if(codeLineElements.size() < 1){
                throw new RuntimeException("代码块元素数量异常");
            }
        }else {
            codeLineElements = previewElements.get(0).children();
        }
        for (Element codeLineElement : codeLineElements) {
            Elements stringElements = codeLineElement.select("[data-slate-string=true]");
            for (Element stringElement : stringElements) {
                if(stringElement.childNodeSize() > 1){
                    throw new RuntimeException("代码行 数量异常");
                }
                Node node = stringElement.childNode(0);
               contentBuilder.append(((TextNode)node).getWholeText());
            }
            contentBuilder.append("\n");
        }
        contentBuilder.append("```").append("\n");
        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
