package com.atguigu.crowd.test;

import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Test
    public void testBcry() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pswd = "123123";
        System.out.println(bCryptPasswordEncoder.matches(pswd,"$2a$10$.Qxamk1sKaFgwsNjI14mMuf0WdsIHUoogZMvo.8lycfoexeHqZLOK"));
    }

}
