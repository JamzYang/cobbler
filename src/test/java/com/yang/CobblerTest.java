package com.yang;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;

/**
 * @author yang
 * Date 2020/3/24 16:42
 */
public class CobblerTest {

    @Test
    public void getLocalArticleList() throws IOException {
        Cobbler cobbler = new Cobbler();
        File[] articleList = cobbler.getLocalArticleList("src/test/resources/src_articles");
        assertEquals(1,articleList.length);
    }

    @Test
    public void processTest() throws IOException {
        Cobbler cobbler = new Cobbler();
        cobbler.process();
    }

}
