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
    public static final String CODE = String.format("[%s=%s]", EnumAttr.CODE.getAttr(), EnumAttr.CODE.getValue());
    public static final String BLOCK_QUOTE = String.format("[%s=%s]", EnumAttr.BLOCK_QUOTE.getAttr(), EnumAttr.BLOCK_QUOTE.getValue());
    public static final String BLOCK_HR = "[data-slate-type=hr]";

    public static final String CODE_PREVIEW = "[data-origin=pm_code_preview]";
    public static final String CODE_LINE = "[data-slate-type=code-line]";

    public static String OBJECT_TEXT = String.format("[%s=%s]", EnumAttr.OBJECT_TEXT.getAttr(), EnumAttr.OBJECT_TEXT.getValue());
    public static String LIST_LINE = String.format("[%s=%s]",EnumAttr.LIST_LINE.getAttr(),EnumAttr.LIST_LINE.getValue());
}
