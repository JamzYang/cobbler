package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.List;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:34 上午
 */
public class ParagraphParser extends BlockParser{
    private static String dataSlateString = "data-slate-string";
    private static String dataSlateObject = "data-slate-object";
    private static String dataSlateLeaf = "data-slate-leaf";
    @Override
    public JSONObject parse(Element element) {

        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        List<Node> nodes = element.childNodes();
        for (Node node : nodes) {
            if(node.hasAttr(dataSlateObject)) {
                List<Node> nodes1 = node.childNodes();
                for (Node node1 : nodes1) {
                    if(node1.hasAttr(dataSlateLeaf)){
                        List<Node> nodes2 = node1.childNodes();
                        for (Node node2 : nodes2) {
                            if(node2.hasAttr(dataSlateString)){
                                List<Node> nodes3 = node2.childNodes();
                                for (Node node3 : nodes3) {
                                    if(node3 instanceof TextNode){
                                        TextNode textNode = (TextNode) node3;
                                        contentBuilder.append(textNode.getWholeText());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        jsonObject.put("content",contentBuilder.toString());
        return jsonObject;
    }
}
