package com.wzh.maoliang;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.wzh.maoliang.Utils.SessionInterceptor;

@Configuration
//当然这段也是百度的
public class SessionConfiguration extends WebMvcConfigurationSupport {
	
	@Resource
    private SessionInterceptor sessionInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry ){
		//需要配置2：----------- 告知拦截器：/static/admin/** 与 /static/user/** 不需要拦截 （配置的是 路径）
		InterceptorRegistration registration = registry.addInterceptor(sessionInterceptor);
		
		registration.addPathPatterns("/**/**");
		
//		这里的拦截跟preHandle那个比较像  区别是preHandle还是会拦下来再放行  但是这个直接就拦不住
		registration.excludePathPatterns(
				"/static/**",				
				"/webapp/signup.html",
				"/login/signUpConfirm",
				"/login/loginConfirm",
				"/login/loginIndex");
//	    registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
	    //网站配置生成器：添加一个拦截器，拦截路径为整个项目
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //需要配置1：----------- 需要告知系统，这是要被当成静态文件的！
        //第一个方法设置访问路径前缀，第二个方法设置资源路径
        registry.addResourceHandler("/static/**","/webapp/**","/favicon.ico").addResourceLocations("classpath:/static/","classpath:/webapp/");
    }
	
}
