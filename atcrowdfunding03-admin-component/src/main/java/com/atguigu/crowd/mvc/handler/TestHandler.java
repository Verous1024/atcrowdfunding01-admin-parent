package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-15 下午 07:00
 */
@Controller
public class TestHandler {
    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @ResponseBody
    @RequestMapping("test/btn1.json")
    public ResultEntity testAJAX() {
        return  ResultEntity.successWithoutData();
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(Model model, HttpServletRequest request) {

        boolean b = CrowdUtil.judgeRequestType(request);
        logger.info("reslut="+b);

        List<Admin> admins = adminService.getAll();
        model.addAttribute("admins", admins);

        System.out.println( 10 /0 );

        return "target";
    }
}
