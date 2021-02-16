package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;

import java.util.List;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-15 下午 04:48
 */
public interface AdminService {
     void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);
}
