package com.yang;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

import static com.yang.HttpUtil.postJson;

/**
 * @author yang
 * Date 2020/3/24 15:55
 */
public class Cobbler {
    private Log log = LogFactory.getLog(Cobbler.class);
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
        String jsonParam = jsonOb.toJSONString();
        JSONObject resJson = JSONObject.parseObject(postJson(commentsUrl, jsonParam, articleUrl));
        List<Comment> commentList = resJson.getJSONObject("data")
                .getJSONArray("list")
                .toJavaList(Comment.class);
        long end = System.currentTimeMillis();
        log.debug("耗时: "+ (end-start)/1000.0 +"秒.");
        log.debug("评论信息已抓取: "+ commentList.size() +"条.\t 耗时: "+ (end-start)/1000.0 +"秒.");
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
