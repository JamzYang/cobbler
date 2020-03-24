package com.yang;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author yang
 * Date 2020/3/24 15:55
 */
public class Cobbler {

    //List<InputStream>
    public void getLocalArticles(String path) throws IOException {
        File dir = new File(path);
        boolean exists = dir.exists();
        if (dir.isDirectory()) {
            FileFilter fileFilter = new SuffixFileFilter("html");
            File[] files = dir.listFiles(fileFilter);
            for (int i = 0; i < files.length; i++) {
                Elements commentsBlock = getCommentsBlock(files[i]);
            }
        }
    }

    public Elements getCommentsBlock(File file) throws IOException {
        JSONObject jsonOb = new JSONObject();
        jsonOb.put("aid", "209108");
        jsonOb.put("prev", "0");
        String jsonStr = jsonOb.toJSONString();
        String baseUrl = "https://time.geekbang.org/column/article/209108";
        Header[] headers = httpGet(baseUrl);
        String responseStr = HttpPostWithJson("https://time.geekbang.org/serv/v1/comments", jsonStr, baseUrl, headers);
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
//                jsonArray.
        List<Comment> comments = jsonArray.toJavaList(Comment.class);
        comments.forEach(comment1 -> {
            String comment_content = comment1.getComment_content();
            System.out.println();
        });

        Document doc = Jsoup.parse(file, "utf-8");
        Elements base = doc.select("head>base");
        //https://time.geekbang.org/column/article/83598
        String articleId = base.attr("href").substring(41);
        String prev = "";
        Elements elements = doc.select(".iconfont");
        elements.stream().map(element -> element.getElementsContainingText("写留言")).filter(e1 -> e1.size() > 0).forEach(e1 -> {
            Elements next = e1.next();
            while (!next.is("ul")) {
                next = next.next();
            }
            Elements lis = next.select("li");
            lis.forEach(li -> {
//                    Elements div = li.select("div");
//                comments.forEach(comment1 -> {
//                    String comment_content = comment1.getComment_content();
//                    System.out.println();
//                });
                Comment comment1 = comments.get(0);
                Element avatarImg = li.child(0);
                avatarImg.attr("src",comment1.getUser_header());
                Element commentBlock = li.child(1);
                Element nickNameEle = commentBlock.child(0);
                Elements nickNameSpan = nickNameEle.select("span");
                //TODO 评论时间暂没修改
                nickNameSpan.html(comment1.getUser_name());
                Element comment = commentBlock.child(1);
                comment.html(comment1.getComment_content());

//                Element reply1 = commentBlock.child(2);
                //TODO 作者回复
/*                Element reply = commentBlock.child(3);
                List<Replay> replies = comment1.getReplies();
                replies.forEach(replay -> {

                });
                reply.select("p").html("作者: "+comment1.getReplies().get(0).getContent());
                System.out.println("div");*/
            });
            System.out.println(e1);
        });
        System.out.println();

        File file1 = new File("src/test/resources/src_articles/aa.html");
        if(!file1.exists()){
            file1.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file1, false);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
        osw.write(doc.html());
        return elements;
    }

    public String post(String url, String json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("url");
        post.setHeader("Content-Type", "application/json");
        StringEntity requestEntity = new StringEntity(json, "utf-8");
        post.setEntity(requestEntity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public  String HttpPostWithJson(String url, String json, String baseUrl, Header[] headers) {

        //
        JSONObject jsonOb = new JSONObject();
        jsonOb.put("aid", "209108");
        jsonOb.put("prev", "5879077364");
        String jsonStr = jsonOb.toJSONString();
        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpClient = HttpClients.createDefault();
//        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(jsonStr, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.addHeader("Content-type", "application/json");
            httpPost.addHeader("Referer",baseUrl);
            httpPost.setEntity(requestEntity);
            String cookie = "";
/*            for (int i = 0; i < headers.length; i++) {
                Header header = headers[i];
                cookie = header.getValue()+cookie+";";
            }
            httpPost.addHeader("Cookie",cookie);*/
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            //第四步：发送HttpPost请求，获取返回值
            CloseableHttpResponse response = httpClient.execute(httpPost);//调接口获取返回值时，必须用此方法
            if(response !=null){
                HttpEntity entity = response.getEntity();
                returnValue = EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return returnValue;
    }

    public  Header[] httpGet(String url) {
        Header[] headers = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse resp = httpClient.execute(httpGet);
            headers = resp.getHeaders("Set-Cookie");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return headers;
    }


    public static String doPost(String url, String jsonstr, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json");
            StringEntity se = new StringEntity(jsonstr);
            se.setContentType("text/json");
            se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
