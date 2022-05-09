package com.yang;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author yang
 * Date 2020/3/24 16:42
 */
public class CobblerTest {

    @Test
    public void getLocalArticleList() throws IOException {
        Cobbler cobbler = new Cobbler();
        Collection<File> localArticleList = cobbler.getLocalArticleList("src/test/resources/src_articles");
        assertEquals(3,localArticleList.size());
    }

    @Test
    public void processTest() throws IOException {
        Cobbler cobbler = new Cobbler();
        cobbler.process();
    }

}
