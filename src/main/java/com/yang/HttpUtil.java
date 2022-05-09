package com.yang;

import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author yang
 * Date 2020/3/25 0:59
 */
@Log4j2
public class HttpUtil {
    public static String postJson(String url, String json, String referer) {
        String returnValue = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.addHeader("Content-type", "application/json");
            httpPost.addHeader("Referer",referer);
            httpPost.setEntity(requestEntity);
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if(response !=null){
                HttpEntity entity = response.getEntity();
                returnValue = EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            log.error("post error",e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

}
