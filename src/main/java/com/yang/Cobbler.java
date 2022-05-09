package com.yang;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;


/**
 * @author yang
 * Date 2020/3/24 15:55
 */
@Log4j2
public class Cobbler {
//    private static String srcDir = "src/test/resources/src_articles";
    private static String srcDir = "/Users/yangshen47/Downloads/归档/学习资料/119-Java 业务开发常见错误 100 例";

    public static void main(String[] args) {
        Cobbler cobbler = new Cobbler();
        cobbler.process();
    }


    public void process() {
        try {
            Collection<File> localArticleList = getLocalArticleList(srcDir);
            for (File articleFile : localArticleList) {
                Article article = loadFile(articleFile);
                article.build();
                File tarArticle = new File(srcDir + "/" + article.getTitle() + ".md");
                if (!tarArticle.exists()) {
                    boolean newFile = tarArticle.createNewFile();
                }
                FileUtils.writeStringToFile(tarArticle,article.getContent().append(article.getComments()).toString(),StandardCharsets.UTF_8);
                log.info("文章【{}】转换完成。",article.getTitle());
            }
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
        article.setTitle(file.getName().replace(".html",""));
        return article;
    }




    /**
     * 获取本地文章列表
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public Collection<File> getLocalArticleList(String path) throws FileNotFoundException {
        File dir = new File(path);
        return FileUtils.listFiles(dir, new String[]{"html"}, true);
    }

}
