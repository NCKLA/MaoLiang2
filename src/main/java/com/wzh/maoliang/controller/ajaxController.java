package com.wzh.maoliang.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ajax")
public class ajaxController {
	
	@RequestMapping("/toslot")
	public String toSlotController() {
		return "redirect:./test/ajaxToManage";
	}
}
