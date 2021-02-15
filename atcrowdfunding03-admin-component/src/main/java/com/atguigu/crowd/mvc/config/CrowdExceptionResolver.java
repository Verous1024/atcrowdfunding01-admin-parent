package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-15 下午 09:27
 */
//@ControllerAdvice 注解用于表明将该类是一个 基于注解的异常处理机制需要的异常处理器
@ControllerAdvice
public class CrowdExceptionResolver {

    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(NullPointerException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * @param viewName 普通请求需要返回的页面
     * @param exception 捕获的异常
     * @param request 请求对象
     * @param response 响应对象
     * @return
     * @throws IOException
     */
    private ModelAndView commonResolve(String viewName,Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1、根据request判断是否是ajax请求
        if (CrowdUtil.judgeRequestType(request)) {

            //2、将异常信息封装到resultEntity中
            ResultEntity<Object> resultEntity = ResultEntity.failedWithData(exception.getMessage());

            //3、创建GSON对象，处理为json数据，然后返回给浏览器
            Gson gson = new Gson();
            String json = gson.toJson(resultEntity);
            response.getWriter().write(json);

            return null;
        }
        //4、非AJAX请求，即普通请求抛出了空指针异常，按下面流程处理
        //创建ModelAn，存储异常信息，存储需要转发的页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", exception);
        modelAndView.setViewName(viewName);

        return modelAndView;
    }
}
