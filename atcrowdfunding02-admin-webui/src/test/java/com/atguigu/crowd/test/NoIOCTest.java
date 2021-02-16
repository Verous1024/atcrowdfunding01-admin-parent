package com.atguigu.crowd.test;

import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-16 上午 11:00
 */
public class NoIOCTest {

    @Test
    public void testMD5() {
        String source = "123123";
        String md5 = CrowdUtil.md5(source);
        System.out.println(md5);
    }


}
