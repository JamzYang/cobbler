package com.yang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.yang.HttpUtil.postJson;

/**
 * @author yang
 * Date 2020/3/24 15:55
 */
@Log4j2
public class Cobbler {
    private Document doc;
    private String articleUrl;
    private String articleId;
    private String commentsUrl = "https://time.geekbang.org/serv/v1/comments";
    private String srcDir = "src/test/resources/src_articles";
    private String tarDir = "src/test/resources/tar_articles";
    private String articleName;
    private Element stdCommentBlock;


    public void process(){
        try {
            File[] articleList = getLocalArticleList(srcDir);
            for (int i = 0; i < articleList.length; i++){
                File article = articleList[i];
                this.articleName = article.getName();
                loadArticle(article);
                parseContent(doc);
                Elements commentListElement = getCommentListElement(doc);
                createStdCommentBlock(commentListElement);

                List<Comment> commentList = grabComments();
                replaceComments(commentListElement,commentList);

                File tarArticle = new File(this.tarDir+"/"+this.articleName);
                if(!tarArticle.exists()){
                    tarArticle.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(tarArticle, false);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                osw.write(doc.html());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    //1.载入文章
    private void loadArticle(File article) throws IOException {
        log.debug("解析文章:  "+article.getName());

        this.doc = Jsoup.parse(article, "utf-8");
        //文章链接 存于<head> <base>标签内
        Elements base = doc.select("head>base");
        //https://time.geekbang.org/column/article/83598
        this.articleUrl = base.attr("href");
        this.articleId = articleUrl.substring(41);
    }

    private static String dataSlateString = "data-slate-string"; //true
    private static String dateSlateObject = "data-slate-object"; //text 文本  //mark  标记
    private static String dataSlateType = "data-slate-type"; //bold 加粗

    private JSONObject parseContent(Document doc){
        Element app = doc.body().getElementById("app");
        Elements select = app.select("[data-slate-editor=true], [autocorrect=off]");
        System.out.println();
        if(select.size()  != 1){
            throw new RuntimeException("正文未识别");
        }
        Element element = select.get(0);
        List<Node> nodes = element.childNodes();
        for (Node node : nodes) {
            List<Node> nodes1 = node.childNodes();
            for (Node node1 : nodes1) {
                if( node1.hasAttr("data-slate-object")){
                    StringBuilder grafString = new StringBuilder();
                   if("text".equals(node1.attr("data-slate-object"))) {
//                       node1.
                       List<Node> nodes2 = node1.childNodes();
                       for (Node node2 : nodes2) {
                           if (node2.hasAttr("data-slate-leaf")) {
                               List<Node> nodes3 = node2.childNodes();
                               for (Node node3 : nodes3) {
                                   if (node3.hasAttr("data-slate-string")) {
                                       List<Node> nodes4 = node3.childNodes();
                                       for (Node node4 : nodes4) {
                                           if (node4 instanceof TextNode) {
                                               TextNode textNode = (TextNode) node4;
                                               String wholeText = textNode.getWholeText();
                                               grafString.append(wholeText);
                                           }
                                       }
                                   }

                               }

                           }
                       }
                   }else if("block".equals(node1.attr("data-slate-object"))){

                   }else {
                       throw new RuntimeException("行内文本未识别");
                   }
                   log.info("段落内容： {}",grafString.toString());
                }

            }
            System.out.println();
        }
        String type = select.attr("data-slate-type");
        if("paragraph".equals(type)){

        }else {
            throw new RuntimeException("块未识别： ");
        }
//        select.s
        return null;
    }
    //2.查找评论列表标签
    // li > img(头像)
    //    > div(评论框)
    //      > div(评论人信息框)
    //          > div(名字+日期)
    //              > div(名字) > span 名字
    //              > div 2019-02-23
    //          > div(点赞块)
    //              >div > i class=iconfont ~ span 点赞数
    //      > div(评论) 内容
    //      > div > span 展开   ~ i class= iconfont
    //      > div(作者回复) > p 回复内容
    private Elements getCommentListElement(Document doc) {
        if(true){
            throw new RuntimeException("test");

        }
        log.debug("开始从Dom树中查找评论框...");
        long start = System.currentTimeMillis();
        Elements elements = doc.select(".iconfont");
        Elements lis = null;
        for (Element element : elements) {
            Elements e1 = element.getElementsContainingText("写留言");
            if(e1.size() > 0){
                Elements next = e1.next();
                while (!next.is("ul")) {
                    next = next.next();
                }
                lis = next.select("li");
                break;
            }
        }
        long end = System.currentTimeMillis();
        log.debug("评论框已找到.\t 耗时: "+ (end-start)/1000.0 +"秒.");
        return lis;
    }
    //3.生成标准评论框. 含评论置顶(如果有), 去除展开, 作者回复
    private void createStdCommentBlock(Elements commentListElement) {
        this.stdCommentBlock = commentListElement.first();
        Element commentBlock = stdCommentBlock.child(1);
        Element commentInfoBar = commentBlock.child(0);

        if(commentBlock.childrenSize() > 2){ //大于2说明有'展开' '作者回复'
            Element child = commentBlock.child(2);
            //判断是不是展开块
            if(child.childNodeSize() == 2 && child.select("span") != null && child.select("#iconfont") != null){
                commentBlock.child(2).remove();
            }
        }

        //是'作者回复'就return
        if(commentBlock.childrenSize() > 2){ //还大于2说明有'作者回复'
            Elements replayBlock = commentBlock.select("div>p");
            if(replayBlock.size() > 0 && replayBlock.html().startsWith("作者回复")){
                return;
            }
        }

        //如果第一个评论里没'作者回复',就遍历找
        for (Element commentEle : commentListElement) {
            Elements replayBlock = commentEle.select("div>p");
            if(replayBlock.size() > 0 && replayBlock.html().startsWith("作者回复")){
                Element replayEle = replayBlock.get(0);
                replayEle.after(commentBlock.child(1));
                return;
            }
        }
    }
    //4.修改评论
    private void replaceComments(Elements commentListElement, List<Comment> commentList) {
        int j = 0;
        // li > img(头像)
        //    > div(评论BOX)
        //      > div(评论信息条)
        //          > div(用户信息块)
        //              > div(名字) > span 名字
        //                          > span 置顶
        //              > div 2019-02-23
        //          > div(点赞块)
        //              >div > i class=iconfont ~ span 点赞数
        //      > div(评论) 内容
        //      > div > span 展开   ~ i class= iconfont
        //      > div(作者回复) > p 回复内容
        commentListElement.clear();
        for(Comment commentEntity: commentList){
            Element commentBlock = this.stdCommentBlock.clone();
            Element avatarImg = commentBlock.child(0);
            avatarImg.attr("src",commentEntity.getUser_header());
            Element commentBox = commentBlock.child(1);
            Element commentInfoBar = commentBox.child(0);
            Element userInfoBlock = commentInfoBar.child(0);
            userInfoBlock.child(0).child(0).html(commentEntity.getUser_name());
            userInfoBlock.child(1).html(commentEntity.getComment_ctime());

            Element likeBlock = commentInfoBar.child(0);
            likeBlock.child(0).select("span").html(commentEntity.getLike_count());

            Element comment = commentBox.child(1);
            comment.html(commentEntity.getComment_content());
            List<Replay> replies = commentEntity.getReplies();
            if(replies != null && replies.size() > 0){  //有 '作者回复'
                Element reply = commentBox.child(2);
                reply.select("p").html("作者: "+commentEntity.getReplies().get(0).getContent());
            }else{//移除标准评论框中的 '作者回复'
                commentBox.child(2).remove();
            }
            commentListElement.add(commentBlock);
        }

    }

    //post 抓取评论信息
    private List<Comment> grabComments(){
        log.debug("开始抓取评论信息...");
        long start = System.currentTimeMillis();
        JSONObject jsonOb = new JSONObject();
        jsonOb.put("aid", articleId);
        jsonOb.put("prev", "0");
        List<Comment> commentList = doGetComments(jsonOb);
        log.info("ping lun :  {}", JSON.toJSONString(commentList));
        long end = System.currentTimeMillis();
        log.debug("耗时: "+ (end-start)/1000.0 +"秒.");
        log.debug("评论信息已抓取: "+ commentList.size() +"条.\t 耗时: "+ (end-start)/1000.0 +"秒.");
        return commentList;
    }

    private List<Comment> doGetComments(JSONObject jsonOb) {
        String jsonParam = jsonOb.toJSONString();
        JSONObject resJson = JSONObject.parseObject(postJson(commentsUrl, jsonParam, articleUrl));
        JSONObject data = resJson.getJSONObject("data");
        List<Comment> commentList = new ArrayList<>(data.getJSONArray("list").toJavaList(Comment.class));
        boolean hasMore = data.getJSONObject("page").getBoolean("more");
        while(hasMore){
            jsonOb.put("prev",commentList.get(commentList.size() - 1).getScore());
            jsonParam = jsonOb.toJSONString();
            resJson = JSONObject.parseObject(postJson(commentsUrl, jsonParam, articleUrl));
            data = resJson.getJSONObject("data");
            commentList.addAll(data.getJSONArray("list").toJavaList(Comment.class));
            hasMore = data.getJSONObject("page").getBoolean("more");
        }
        return commentList;
    }


    /**
     * 获取本地文章列表
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public File[] getLocalArticleList(String path) throws FileNotFoundException {
        File dir = new File(path);
        boolean exists = dir.exists();
        File[] files = {};
        if (dir.isDirectory()) {
            FileFilter fileFilter = new SuffixFileFilter("html");
            files = dir.listFiles(fileFilter);
        }else{
            log.error(path+"不存在", new FileNotFoundException());
        }
        return files;
    }

}
