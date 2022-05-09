package com.yang;

import com.alibaba.fastjson.JSONObject;
import com.yang.parser.BlockParser;
import com.yang.parser.CommentsBuilder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.jsoup.Jsoup;
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


/**
 * @author yang
 * Date 2020/3/24 15:55
 */
@Log4j2
public class Cobbler {

    public void process() {
        try {
            String srcDir = "src/test/resources/src_articles";
            File[] articleList = getLocalArticleList(srcDir);
            for (int i = 0; i < articleList.length; i++) {
                File articleFile = articleList[i];
                String articleName = articleFile.getName();
                Article article = loadFile(articleFile);
                parseContent(article);

                CommentsBuilder commentsBuilder = new CommentsBuilder(article);
                commentsBuilder.build();

                String tarDir = "src/test/resources/tar_articles";
                File tarArticle = new File(tarDir + "/" + articleName);
                if (!tarArticle.exists()) {
                    tarArticle.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(tarArticle, false);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
//                osw.write(doc.html());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //1.载入文章
    private Article loadFile(File file) throws IOException {
        Article article = new Article();
        log.debug("解析文章:  " + file.getName());
        article.setDoc(Jsoup.parse(file, "utf-8"));
        //文章链接 存于<head> <base>标签内
        Elements base = article.getDoc().select("head>base");
        //https://time.geekbang.org/column/article/83598
        article.setArticleUrl(base.attr("href"));
        article.setArticleId(article.getArticleUrl().substring(41));
        return article;
    }

    private void parseContent(Article article) {
        Element app = article.getDoc().body().getElementById("app");
        Elements contentsElements = app.select("[data-slate-editor=true], [autocorrect=off]");
        if (contentsElements.size() != 1) {
            throw new RuntimeException("正文未识别");
        }
        List<JSONObject> list = new ArrayList<>();
        Element contentElement = contentsElements.get(0);
        Elements blockElements = contentElement.children();
        for (Element blockElement : blockElements) {
            BlockParser blockParser = BlockParser.createBlockParser(blockElement);
            list.add(blockParser.parse());
        }
        StringBuilder contentSB = new StringBuilder();
        list.forEach(item -> contentSB.append(item.getString("content")));
        article.setContent(contentSB);
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
