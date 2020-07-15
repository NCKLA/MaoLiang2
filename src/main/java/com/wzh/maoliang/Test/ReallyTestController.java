package com.wzh.maoliang.Test;

import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/realTest")
public class ReallyTestController {
	public static Random random = new Random();
	
	@RequestMapping("/draw")
    @Async("taskExecutor")
    public String doTaskOne(HttpServletResponse response) throws Exception {
        System.out.println("开始做任务一");
        long start = System.currentTimeMillis();
        
		/* 抽题    计划是抽题之后直接重定向到提交 */
        response.sendRedirect("../test/draw?userId=111&hasLabeled=T&hisDataId=4396");
		/* 提交 ，为的是db里面他标过这题                        （但是怎么恰到抽题里的分配数据呢）*/
        
        
        long end = System.currentTimeMillis();
        System.out.println("完成任务一，耗时：" + (end - start) + "毫秒");
        
		return null;
    }
}
