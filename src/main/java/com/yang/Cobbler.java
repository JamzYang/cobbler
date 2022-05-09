package com.yang;

import com.alibaba.fastjson.JSONObject;
import com.yang.parser.CommentsBuilder;
import com.yang.parser.HeadingParser;
import com.yang.parser.ImageParser;
import com.yang.parser.ListParser;
import com.yang.parser.ParagraphParser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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


    public void process() {
        try {
            File[] articleList = getLocalArticleList(srcDir);
            for (int i = 0; i < articleList.length; i++) {
                File article = articleList[i];
                this.articleName = article.getName();
                loadArticle(article);
                StringBuilder contentSB = parseContent(doc);

                List<Comment> commentList = grabComments();
                CommentsBuilder commentsBuilder = new CommentsBuilder();
                StringBuilder comments = commentsBuilder.build(commentList);
                contentSB.append(comments);
                System.out.println(contentSB.toString());

                File tarArticle = new File(this.tarDir + "/" + this.articleName);
                if (!tarArticle.exists()) {
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
        log.debug("解析文章:  " + article.getName());

        this.doc = Jsoup.parse(article, "utf-8");
        //文章链接 存于<head> <base>标签内
        Elements base = doc.select("head>base");
        //https://time.geekbang.org/column/article/83598
        this.articleUrl = base.attr("href");
        this.articleId = articleUrl.substring(41);
    }

    private StringBuilder parseContent(Document doc) {
        Element app = doc.body().getElementById("app");
        Elements contentsElements = app.select("[data-slate-editor=true], [autocorrect=off]");
        if (contentsElements.size() != 1) {
            throw new RuntimeException("正文未识别");
        }
        List<JSONObject> list = new ArrayList<>();
        Element contentElement = contentsElements.get(0);
        Elements blockElements = contentElement.children();
        for (Element blockElement : blockElements) {
            if (blockElement.is(SelectUtil.PARAGRAPH)) {
                ParagraphParser paragraphParser = new ParagraphParser();
                JSONObject parse = paragraphParser.parse(blockElement);
                list.add(parse);
            }else if(blockElement.is(SelectUtil.H2)){
                HeadingParser headingParser = new HeadingParser();
                JSONObject parse = headingParser.parse(blockElement);
                list.add(parse);
            }else if(blockElement.is(SelectUtil.LIST)){
                ListParser listParser = new ListParser();
                list.add(listParser.parse(blockElement));
            }else if(blockElement.is(SelectUtil.IMAGE)){
                ImageParser imageParser = new ImageParser();
                list.add(imageParser.parse(blockElement));
            }else {
                throw new RuntimeException("未识别的 block");
            }
        }
        StringBuilder contentSB = new StringBuilder();
        list.forEach(item -> contentSB.append(item.getString("content")));
//        select.s
        return contentSB;
    }


    //post 抓取评论信息
    private List<Comment> grabComments() {
        log.debug("开始抓取评论信息...");
        long start = System.currentTimeMillis();
        JSONObject jsonOb = new JSONObject();
        jsonOb.put("aid", articleId);
        jsonOb.put("prev", "0");
        List<Comment> commentList = doGetComments(jsonOb);
        System.out.println(JSONObject.toJSONString(commentList));
        long end = System.currentTimeMillis();
        log.debug("耗时: " + (end - start) / 1000.0 + "秒.");
        log.debug("评论信息已抓取: " + commentList.size() + "条.\t 耗时: " + (end - start) / 1000.0 + "秒.");
        return commentList;
    }

    private List<Comment> doGetComments(JSONObject jsonOb) {
        String jsonParam = jsonOb.toJSONString();
        String respResult = postJson(commentsUrl, jsonParam, articleUrl);
        if(StringUtils.isBlank(respResult)){
            throw new RuntimeException("获取评论信息失败");
        }
        JSONObject resJson = JSONObject.parseObject(respResult);
        JSONObject data = resJson.getJSONObject("data");
        List<Comment> commentList = new ArrayList<>(data.getJSONArray("list").toJavaList(Comment.class));
        boolean hasMore = data.getJSONObject("page").getBoolean("more");
        while (hasMore) {
            jsonOb.put("prev", commentList.get(commentList.size() - 1).getScore());
            resJson = JSONObject.parseObject(respResult);
            data = resJson.getJSONObject("data");
            commentList.addAll(data.getJSONArray("list").toJavaList(Comment.class));
            hasMore = data.getJSONObject("page").getBoolean("more");
        }
        return commentList;
    }


    /**
     * 获取本地文章列表
     *
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
        } else {
            log.error(path + "不存在", new FileNotFoundException());
        }
        return files;
    }

}
