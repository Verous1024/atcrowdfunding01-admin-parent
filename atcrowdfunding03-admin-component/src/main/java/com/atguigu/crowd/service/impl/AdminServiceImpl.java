package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.Exception.LoginFailedException;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-15 下午 04:49
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public void saveAdmin(Admin admin) {
        adminMapper.insertSelective(admin);
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(null);
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        //1、根据登陆账号查询Admin
        //1.1 创建AdminExample对象
        AdminExample example = new AdminExample();
        //2.2 创建Criteria对象
        AdminExample.Criteria criteria = example.createCriteria();
        //2.3 在Criteria对象中封装条件
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> list = adminMapper.selectByExample(example);
        //2、判断Admin对象是否为空
        if (list == null || list.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if (list.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        Admin admin = list.get(0);

        //3、如果Admin对象为空，抛出异常
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        //4、如果Admin对象不为空，获取数据库中的密码(数据库中存储的是MD5加密的结果！）
        //5、将登陆的密码进行MD5加密
        String userPswdMD5DB = admin.getUserPswd();
        String userPswdMD5 = CrowdUtil.md5(userPswd);
        //6、加密后进行密文比较，相等就返回admin对象
        if (!Objects.equals(userPswdMD5, userPswdMD5DB)) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //7、不相等返回抛出异常
        return admin;
    }
}
