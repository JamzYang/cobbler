package com.yang;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author yang
 * Date 2020/3/24 16:42
 */
public class CobblerTest {

    @Test
    public void getLocalArticles() throws IOException {
        Cobbler cobbler = new Cobbler();
        cobbler.getLocalArticles("src/test/resources/src_articles");
//        String s = cobbler.httpGet("https://time.geekbang.org/column/article/209108");
        System.out.println();
    }
}
