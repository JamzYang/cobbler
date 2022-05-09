package com.yang.parser;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.List;
import java.util.Optional;

/**
 * @author yangshen47
 * @description
 * @date 2022/5/9 2:34 上午
 */
public class ListParser extends BlockParser{
    private static String dataSlateString = "data-slate-string";
    private static String dataSlateObject = "data-slate-object";
    private static String dataSlateType = "data-slate-type";
    private static String dataSlateLeaf = "data-slate-leaf";
    @Override
    public JSONObject parse(Element element) {

        JSONObject jsonObject = new JSONObject();
        StringBuilder contentBuilder = new StringBuilder();
        List<Node> nodes = element.childNodes();
        for (Node node : nodes) {
            if(node.hasAttr(dataSlateType) && "list-line".equals(node.attr(dataSlateType))) {
                contentBuilder.append("- ");
                List<Node> nodes1 = node.childNodes();
                for (Node node1 : nodes1) {
                    if(node1.hasAttr(dataSlateObject) && "text".equals(node1.attr(dataSlateObject))){
                        List<Node> nodes2 = node1.childNodes();
//                        nodes2.stream().filter(item -> "text".equals(item.attr(dataSlateObject)))
//                                .map(item -> "true".equals(item.attr()) )
                        for (Node node2 : nodes2) {
                            if("true".equals(node2.attr(dataSlateLeaf))){
                                List<Node> nodes3 = node2.childNodes();
                                for (Node node3 : nodes3) {
                                    if(node3.hasAttr(dataSlateString)){
                                        List<Node> nodes4 = node3.childNodes();
                                        for (Node node5 : nodes4) {
                                            if(node5 instanceof TextNode){
                                                TextNode textNode = (TextNode) node5;
                                                contentBuilder.append(textNode.getWholeText()).append("\n");
                                            }
                                        }
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
