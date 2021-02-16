package com.atguigu.crowd.mvc.interceptor;

import com.atguigu.crowd.Exception.AccessForbiddenExcepiton;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-16 下午 01:24
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1、通过requese对象获取seesion对象
        HttpSession session = request.getSession();
        //2、尝试重seesion域中获取admin对象
        Admin admin =(Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        //3、判断admin是否为空
        if (admin == null) {
            //4、如果admin为空，抛出异常AccessForbiddenExcepiton
            throw new AccessForbiddenExcepiton(CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
        }
        //5、如果admin不为空，返回true
        return true;
    }
}
