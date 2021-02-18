package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-18 上午 09:45
 */
@Controller
public class AssignHandler {

    @Autowired
    RoleService roleService;

    @Autowired
    AdminService adminService;

    @RequestMapping(value="/assign/do/role/assign.html",method = RequestMethod.POST)
    public String saveAdminRoleRelationship(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") Integer keyword,
            @RequestParam(value = "roleIdList", required = false) List<Integer> roleIdList) {
        adminService.saveAdminRoleRelationship(adminId, roleIdList);
        if (keyword == null) {
            return "redirect:/admin/get/page.html?pageNum=" + pageNum;
        }
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
            Model model) {

        //1、查询已分配的角色
       List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

       //2、查询未分配的角色
        List<Role> unAssignedRoleList = roleService.getUnassignedRole(adminId);

        //3、封装搭配模型上
        model.addAttribute("assignedRoleList", assignedRoleList);
        model.addAttribute("unAssignedRoleList", unAssignedRoleList);
        return "assign-role";
    }

}
