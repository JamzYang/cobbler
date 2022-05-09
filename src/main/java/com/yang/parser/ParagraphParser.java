package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.SelectUtil;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:34 上午
 */
@NoArgsConstructor
public class ParagraphParser extends BlockParser{

    public ParagraphParser(Element blockElement) {
       super(blockElement);
    }


    @Override
    public JSONObject parse() {

        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        Elements select = getBlockElement().select(SelectUtil.OBJECT_TEXT);
        for (Element element1 : select) {
            try {

                ObjectTextParser objectTextParser = new ObjectTextParser(element1);
                String text = objectTextParser.parseText();
                boolean doubleStarEnd = (contentBuilder.length() > 2 && "**".equals(contentBuilder.substring(contentBuilder.length()-2, contentBuilder.length())));
                boolean doubleStarStart = (text.length() > 2 && "**".equals(text.substring(0, 2)));
                if(doubleStarEnd && doubleStarStart){
                    String cutEnd = contentBuilder.substring(0, contentBuilder.length() - 2);
                    String cutStart = text.substring(2);
                    contentBuilder = new StringBuilder();
                    contentBuilder.append(cutEnd).append(cutStart);
                }else{
                    contentBuilder.append(text);
                }
            }catch (Exception e){
                System.out.println(element1);
            }

        }

        contentBuilder.append("\n");
        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
