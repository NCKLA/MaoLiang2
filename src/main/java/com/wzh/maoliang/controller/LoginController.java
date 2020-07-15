package com.wzh.maoliang.controller;

import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wzh.maoliang.ServiceImpl.UserServiceImpl;
import com.wzh.maoliang.entity.User;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Resource
	private UserServiceImpl userService;
	
	/* 登录验证，如果成功则跳转到选择任务界面；不成功弹个提示 */
	@RequestMapping("/loginConfirm")
	private String loginConfirm(User user,HttpServletRequest request) {
		System.out.println("你传的User："+user);
		Optional<User> userOp= userService.findByUserIdAndPassWord(user.getUserId(), user.getPassWord());
		if(userOp.isPresent())   {         
			request.getSession().setAttribute("user", userOp.get());
	        //先添加到session,在跳转
			System.out.println("准备去selectTask");
	        return "redirect:../webapp/SelectTask.html";
		}
		else return "redirect:loginIndex?error="+"true";
	}
	
	
//	@ResponseBody
//	@RequestMapping("/selectTask")
//	private ModelAndView selectTask() {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("SelectTask");
//		return mav;
//	}
	
	/*
	 * 最先见到的登录界面。由于两种特殊情况所以有两个参数 
	 * error：用户名密码错误 
	 * unsigned：未登录就访问后面的其他网页，被跳转到这里
	 */
	@ResponseBody
	@RequestMapping("/loginIndex")
	private ModelAndView loginIndex(String error,String unsigned) {
		ModelAndView mav = new ModelAndView();
		if(null != error) {
			System.out.println("进入loginIndex,传过来这个error："+error);
			mav.addObject("wrong", error);
		}
		if(null != unsigned) {
			System.out.println("unsigned=="+unsigned);
			mav.addObject("unsigned", unsigned);
			System.out.println("进入loginIndex,传过来这个unsigned："+unsigned);
		}
		mav.setViewName("login");
		return mav;
	}
	
	/* 【mapping】去注册页面，如果注册页面是html，这个可以不要 */
	
	
	/* 【mapping】注册验证 ,然后重定向到*/
	@RequestMapping("/signUpConfirm")
	private String signUpConfirm(User u) {
		userService.save(u);
		return "redirect:loginIndex";
	}
	

	
	
	/* 百度来作为参考 */
	public String yanzheng(User user ,HttpServletRequest request){
        //只是密码的简单判断，哈哈。当然也可以连数据判断
        if("1234".equals(user.getPassWord())){
            request.getSession().setAttribute("users", user);
            //先添加到session,在跳转
            return "index";
        }else {
            return "login";
        }
    }
}
