package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @Autowired
    AuthService authService;

    @ResponseBody
    @RequestMapping("assign/do/role/assign/auth.json")
    public ResultEntity<String> saveRoleAuthRelathinship(
            @RequestBody Map<String,List<Integer>> map) {
        authService.saveRoleAuthRelathinship(map);

        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedIdByRoleId(
            @RequestParam("roleId") Integer roleId) {
        List<Integer> roleIdList = authService.getAssignedIdByRoleId(roleId);
        return ResultEntity.successWithData(roleIdList);
    }

    @ResponseBody
    @RequestMapping("/assign/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth() {

        List<Auth> authList = authService.getAll();

        return ResultEntity.successWithData(authList);
    }


    @RequestMapping(value = "/assign/do/role/assign.html", method = RequestMethod.POST)
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
