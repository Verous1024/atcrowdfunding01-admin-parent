package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-15 下午 04:49
 */
@Service
public class AdminServiceImpl  implements AdminService {

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
}
