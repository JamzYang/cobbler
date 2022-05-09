package com.yang.parser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:34 上午
 */
@Getter
@NoArgsConstructor
public abstract class TextParser implements Parser{
    private Element textElement;

    public TextParser(Element textElement) {
        this.textElement = textElement;
    }

    public final String parseText(){
       return parse().getString("content");
    }

}
