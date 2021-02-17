package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-17 下午 04:37
 */
@Controller
public class MenuHandler {

    @Autowired
    MenuService menuService;
}
