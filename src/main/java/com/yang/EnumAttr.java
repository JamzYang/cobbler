package com.yang;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 11:21 上午
 */
@AllArgsConstructor
@Getter
public enum EnumAttr {
    BLOCK("data-slate-object","block"),

    PARAGRAPH("data-slate-type","paragraph"),
    LIST("data-slate-type","list"),
    IMAGE("data-slate-type","image"),
    HEADING("data-slate-type","heading"),
    CODE("data-slate-type","pre"),
    BLOCK_QUOTE("data-slate-type","block-quote"),

    LIST_LINE("data-slate-type","list-line"),

    OBJECT_TEXT("data-slate-object","text"),
    OBJECT_MARK("data-slate-object","mark"),
    BOLD("data-slate-type","bold"),
    LEAF("data-slate-leaf","true"),
    STRING("data-slate-string","true"),


    IMAGE_SRC("data-savepage-src",""),
    CODE_LANGUAGE("data-code-language",""),
    ;
    private String attr;
    private String value;

}
