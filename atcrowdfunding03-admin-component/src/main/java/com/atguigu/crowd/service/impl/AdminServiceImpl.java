package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.Exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.Exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.Exception.LoginFailedException;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.Date;
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
        //1、密码加密
        String userPswd = admin.getUserPswd();
        String md5 = CrowdUtil.md5(userPswd);
        admin.setUserPswd(md5);

        //2、生成创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm;ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);
        try {
            adminMapper.insertSelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
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
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED_WITHOUT_ACCT + "&" + loginAcct + "&" + userPswd);
        }

        if (list.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE + "&" + loginAcct + "&" + userPswd);
        }

        Admin admin = list.get(0);

        //3、如果Admin对象为空，抛出异常
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED_WITHOUT_ACCT + "&" + loginAcct + "&" + userPswd);
        }

        //4、如果Admin对象不为空，获取数据库中的密码(数据库中存储的是MD5加密的结果！）
        //5、将登陆的密码进行MD5加密
        String userPswdMD5DB = admin.getUserPswd();
        String userPswdMD5 = CrowdUtil.md5(userPswd);
        //6、加密后进行密文比较，相等就返回admin对象
        if (!Objects.equals(userPswdMD5, userPswdMD5DB)) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED + "&" + loginAcct + "&" + userPswd);
        }
        //7、不相等返回抛出异常
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

        return new PageInfo<>(list);

    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public boolean verifyLoginAcct(String loginAcct) {
        //1、根据登陆账号查询Admin
        //1.1 创建AdminExample对象
        AdminExample example = new AdminExample();
        //2.2 创建Criteria对象
        AdminExample.Criteria criteria = example.createCriteria();
        //2.3 在Criteria对象中封装条件
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> list = adminMapper.selectByExample(example);
        if (list.size() >= 1) {
            return true; //true表示重复
        }
        return false; //false 表示不重复
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    /**
     * @param admin
     * @return 返回true，表示更新的是用户本体,且密码非当前密码，返回false，表示更新的非本用户，或者密码没有修改
     */
    @Override
    public boolean updateAdmin(Admin admin) {
        boolean flag = true;
        String newUserPswd = admin.getUserPswd();
        if (newUserPswd == null || newUserPswd == "") {
            //不需要修改密码
            admin.setUserPswd(null);
            flag = false;
        } else {
            //需要修改密码,newUserPswd先加密
            newUserPswd=CrowdUtil.md5(newUserPswd);
            //获取数据库的该对象的密码
            String oldUserPswd = adminMapper.selectByPrimaryKey(admin.getId()).getUserPswd();
            //比较 不相等就更新
            if (!oldUserPswd.equals(newUserPswd)) {
                flag = true;
            } else {
                flag = false;
            }
        }
        System.out.println(flag);
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }finally {
            return flag;
        }
    }
}
