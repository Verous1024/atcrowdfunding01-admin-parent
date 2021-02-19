package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-19 上午 11:55
 */
@Component
public class CrowdUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1、根据账号名称查询admin对象
        Admin admin = adminService.getAdminByLoginAcct(username);
        //2、获取adminId
        Integer adminId = admin.getId();
        //3、根据adminId查询角色信息
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        //4、根据adminId查询权限信息
        List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);
        //5、创建集合对象用来存储GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();

        //遍历角色的名字和权限的名字加入到集合中
        for (Role role : assignedRoleList) {
            String roleName = "ROLE_"+role.getName();
            authorities.add(new SimpleGrantedAuthority(roleName));
        }
        for (String authName : authNameList) {
            authorities.add(new SimpleGrantedAuthority(authName));
        }

        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);

        return securityAdmin;
    }
}
