package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import com.yang.EnumAttr;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.List;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:34 上午
 */
public class ObjectTextParser  extends TextParser{

    @Override
    public JSONObject parse(Element element) {
        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        List<Node> nodes = element.childNodes();
        for (Node node1 : nodes) {
//            if(node.hasAttr(EnumAttr.OBJECT_TEXT.getAttr())) {
//                List<Node> nodes1 = node.childNodes();
//                for (Node node1 : nodes1) {
                    if(node1.hasAttr(EnumAttr.LEAF.getAttr())){
                        List<Node> nodes2 = node1.childNodes();
                        for (Node node2 : nodes2) {
                            if(node2.hasAttr(EnumAttr.STRING.getAttr())){
                                List<Node> nodes3 = node2.childNodes();
                                for (Node node3 : nodes3) {
                                    if(node3 instanceof TextNode){
                                        TextNode textNode = (TextNode) node3;
                                        contentBuilder.append(textNode.getWholeText());
                                    }
                                }
                            }else if(node2.hasAttr(EnumAttr.OBJECT_MARK.getAttr())){
                                List<Node> nodes3 = node2.childNodes();
                                for (Node node4 : nodes3) {
                                    if(node4.hasAttr(EnumAttr.STRING.getAttr())){
                                        boolean boldStart = contentBuilder.length() < 2 || (contentBuilder.length() > 2 && !"**".equals(contentBuilder.substring(contentBuilder.length()-2, contentBuilder.length())));
                                        if(boldStart){
                                            contentBuilder.append("**");
                                        }
                                        List<Node> nodes4 = node4.childNodes();
                                        for (Node node3 : nodes4) {
                                            if(node3 instanceof TextNode){
                                                TextNode textNode = (TextNode) node3;
                                                contentBuilder.append(textNode.getWholeText());
                                            }
                                        }
                                        if(contentBuilder.length() > 2 && !"**".equals(contentBuilder.substring(contentBuilder.length()-2, contentBuilder.length()))){
                                            contentBuilder.append("**");
                                        }
                                    }
                                }
                            }
                        }
                    }
//                }
//            }
        }

        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }

    public static void main(String[] args) {
        StringBuilder contentBuilder = new StringBuilder("**好吧**");
        String lastChar = contentBuilder.substring(contentBuilder.length() - 2, contentBuilder.length());
        if(!"**".equals(contentBuilder.substring(contentBuilder.length()-2, contentBuilder.length()))){
            contentBuilder.append("**");
        }
        String substring = contentBuilder.substring(0, 2);
        System.out.println();
    }
}
