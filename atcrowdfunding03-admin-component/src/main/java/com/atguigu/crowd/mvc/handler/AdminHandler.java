package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-16 上午 11:14
 */
@Controller
public class AdminHandler {
    @Autowired
    AdminService adminService;

    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword,
                         HttpSession session) {

        boolean result = adminService.updateAdmin(admin);
        if (result) {
            session.removeAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
            return "redirect:admin/to/login.html";
        } else {
            System.out.println("????");
            return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
        }
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            Model model) {
        Admin admin = adminService.getAdminById(adminId);
        model.addAttribute("admin", admin);
        return "admin-edit";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/verifyLoginAcct.json", method = RequestMethod.POST)
    public boolean verifyLoginAcct(@RequestParam("loginAcct") String loginAcct) {
        System.out.println("进入验证中！");
        return adminService.verifyLoginAcct(loginAcct);
    }

    @RequestMapping("/admin/save.html")
    public String save(Admin admin) {
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") Integer keyword) {
        adminService.remove(adminId);
        if (keyword == null) {
            return "redirect:/admin/get/page.html?pageNum=" + pageNum;
        }
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }


    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            Model model
    ) {
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        model.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam(value = "loginAcct") String loginAcct,
                          @RequestParam(value = "userPswd") String userPswd,
                          HttpSession session) {
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);

        return "redirect:/admin/to/main/page.html";
    }
}
