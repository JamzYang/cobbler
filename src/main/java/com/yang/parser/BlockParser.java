package com.yang.parser;

import com.yang.SelectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:33 上午
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class BlockParser implements Parser {
    private Element blockElement;

   public static BlockParser createBlockParser(Element blockElement) {
        if (blockElement.is(SelectUtil.PARAGRAPH)) {
            return new ParagraphParser(blockElement);
        } else if (blockElement.is(SelectUtil.H2)) {
            return new HeadingParser(blockElement);
        } else if (blockElement.is(SelectUtil.LIST)) {
            return new ListParser(blockElement);
        } else if (blockElement.is(SelectUtil.IMAGE)) {
            return new ImageParser(blockElement);
        } else if (blockElement.is(SelectUtil.CODE)) {
            return new CodeParser(blockElement);
        } else {
            throw new RuntimeException("未识别的 block ===> " + blockElement.getClass());
        }
    }
}
