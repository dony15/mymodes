package com.dony15.shop.utils;

/**
 * @author DonY15
 * @description
 * @create 2018\6\19 0019
 */
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//这是一个拦截器，是springmvc封装的
public class EncodeInterceptor implements HandlerInterceptor {
    //返回true就是放行
    //返回false就是拦截
    //这个方法多数情况下用于权限校验
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        return true;
    }

    //在返回ModelAndView之前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    //返回ModelAndView之后
    //日志
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        if(e!=null){
            e.printStackTrace();
        }
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }
}

