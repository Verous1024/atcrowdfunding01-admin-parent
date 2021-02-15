package com.atguigu.crowd.test;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-15 下午 02:43
 */

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

// 指定 Spring 给 Junit 提供的运行器类
@RunWith(SpringJUnit4ClassRunner.class)
// 加载 Spring 配置文件的注解
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Test
    public void testTx() {
        Admin admin = new Admin(null, "tom", "123123", "汤姆", "tom@qq.com", null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void testDataSource() throws SQLException {
    //1.通过数据源对象获取数据源连接
        Connection connection = dataSource.getConnection();
    //2.打印数据库连接
        System.out.println(connection);
    }

    @Test
    public void testAdminMapper() {
        Admin admin = new Admin(null, "tom", "123123", "汤姆", "tom@qq.com", null);
        int i = adminMapper.insertSelective(admin);
        System.out.println("影响条数："+i);
    }
}