package com.wzh.maoliang.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//百度摘的，本质也是靠session分辨用户有没有登录。但是看起来可以覆盖所有页面的样子
@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
            }
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
            }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {    	
        System.out.println("进入拦截器，路径为："+request.getRequestURI());
        //普通路径放行(可以直接在SessionConfiguration里面的exclude里面加  加了之后都不会进这里面  暂且先注释掉)
//        if (request.getRequestURI().equalsIgnoreCase("/login/signUpConfirm") || 
//        		request.getRequestURI().contentEquals("/login/loginIndex") ||
//        		request.getRequestURI().contentEquals("/webapp/signup.html") ||
//        		request.getRequestURI().contentEquals("/login/loginConfirm")) {
//            System.out.println("通过检查");
//        	return true;
//            }        
        //权限路径拦截
        
        if (request.getRequestURI().equalsIgnoreCase("/error")) {        	
        	System.out.println("不知道怎么回事就有个/error");
        	return false;
        }
        Object object = request.getSession().getAttribute("user");
        if (null == object) {
            System.out.println("没有权限，请先登录");
            response.sendRedirect("/login/loginIndex?unsigned="+"true");
            return false;
        }else System.out.println("已登录 用户是"+object.toString());
        	
        System.out.println("总之放行了");
        return true;
    }



}
