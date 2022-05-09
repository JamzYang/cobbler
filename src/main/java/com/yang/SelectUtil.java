package com.yang;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 12:33 下午
 */
public class SelectUtil {
    public static final String PARAGRAPH = String.format("[%s=%s]", EnumAttr.PARAGRAPH.getAttr(), EnumAttr.PARAGRAPH.getValue());
    public static final String H2 = String.format("[%s=%s]", EnumAttr.HEADING.getAttr(), EnumAttr.HEADING.getValue());
    public static final String LIST = String.format("[%s=%s]", EnumAttr.LIST.getAttr(), EnumAttr.LIST.getValue());
    public static final String IMAGE = String.format("[%s=%s]", EnumAttr.IMAGE.getAttr(), EnumAttr.IMAGE.getValue());

    public static String OBJECT_TEXT = String.format("[%s=%s]", EnumAttr.OBJECT_TEXT.getAttr(), EnumAttr.OBJECT_TEXT.getValue());
    public static String LIST_LINE = String.format("[%s=%s]",EnumAttr.LIST_LINE.getAttr(),EnumAttr.LIST_LINE.getValue());
}
