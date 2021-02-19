package com.atguigu.crowd.mvc.config;


import com.atguigu.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Descriptions:
 *
 * @author Verous
 * @version 1.0 2021-02-18 下午 08:58
 */

//注意：这个类放在了com.atguigu.crowd.mvc的包下，然后通过@Configuration注解，会被自动注入到Spring MVC的容器中！！！！
    //认识到：@Configuration能够向容器中注入Bean;
    //Spring Security的目标就是控制web的各种请求/url/handler,因此我们需要注入到Spring MVC容器中
@Configuration
@EnableWebSecurity
//启用全家方法权限控制功能：并且设置prePostEnabled =韬略，保证@PreAuthority，@PostAuthority....
//例如：@PreAuthorize("hasRole('部长')")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
       /* builder
                .inMemoryAuthentication()
                .withUser("tom")
                .password("123123")
                .roles("ADMIN");*/
       //使用数据库登录
       builder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests()
                .antMatchers("/admin/to/login/page.html")
                .permitAll()
                .antMatchers("/bootstrap/**")
                .permitAll()
                .antMatchers("/crowd/**")
                .permitAll()
                .antMatchers("/css/**")
                .permitAll()
                .antMatchers("/fonts/**")
                .permitAll()
                .antMatchers("/img/**")
                .permitAll()
                .antMatchers("/jquery/**")
                .permitAll()
                .antMatchers("/layer/**")
                .permitAll()
                .antMatchers("/script/**")
                .permitAll()
                .antMatchers("/ztree/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
/*               .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        request.setAttribute("exception",new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED)); //设置异常信息
                        request.getRequestDispatcher("/WEB_INF/system-error.jsp").forward(request,response); //请求转发过去
                    }
                })
                .and()*/
                .csrf()
                .disable()
                .formLogin()
                .loginPage("/admin/to/login/page.html")
                .loginProcessingUrl("/security/do/login.html")
                .defaultSuccessUrl("/admin/to/main/page.html")
                .usernameParameter("loginAcct")
                .passwordParameter("userPswd")
                .and()
                .logout()
                .logoutUrl("/security/do/logout.html")
                .logoutSuccessUrl("/admin/to/login/page.html")
                ;
    }
}
