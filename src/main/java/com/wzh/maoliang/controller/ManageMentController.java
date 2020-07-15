package com.wzh.maoliang.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wzh.maoliang.Utils.ObjectFileIOUtils;


@Controller
@RequestMapping("/manage")
public class ManageMentController {
	
//	【mapping】
	private List<String> singleSlot;
	
	private ObjectFileIOUtils fileUtils = new ObjectFileIOUtils();
	
	private String slotMenuFilename = "slotValueMenu";
	/* slot value的二级下拉菜单内容 */
//	private Map<String,List<String>> slotMenu = new HashMap<String,List<String>>();
	@SuppressWarnings("unchecked")
	private Map<String,List<String>> slotMenu = (Map<String, List<String>>) fileUtils.readObjectFromFile(slotMenuFilename);
	
	private Map<String,String> newSlotMenu = new HashMap<String,String>();
	
	/* action的二级下拉菜单内容 */
//	private Map<String,List<String>> actionMenu;
//	
//	private Map<String,String> newActionMenu;
	
	/* 【mapping】通过这个方法进到management页面，要给的信息都给出去 */
	@ResponseBody
	@RequestMapping("/overview")
	public ModelAndView overview() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("slotMenu", slotMenu);
		mav.addObject("newSlotMenu", newSlotMenu);
		mav.addObject("", null);
		
		/* 中间省略一大段要加的对象 */
		mav.setViewName("");
		return mav;		
	}
	
	
	/* 【mapping】用户自定义标签传入，进行判断  合法且不存在重复  则先发到管理页面 等我审核则  , 存在则  B */
	@RequestMapping("/Judge")
	public String JudgeNewDIYSlot(String entityType,String entity) {
		
		if(!slotMenu.containsKey(entityType)) return "redirect:NewSlotDenied?message="+"不存在该slot";
		
		else if(slotMenu.get(entityType).contains(entity)) return "redirect:NewSlotDenied?message="+"已存在该value";
		
		/* 更新newSlotMenu，并且刷新到management页面*/
		newSlotMenu.put(entityType, entity);	
		
		
		/** 不对 有大问题 这样写只要有人自定义新slot主页就会刷新 应该先存起来    
		 * 但是又要求能返回错误信息  这就很尴尬了
		 * 现在想法是  弄一个提交成功待审核的提示  然后从提示返回上一页*/
		return "redirect:overview";
	}	
	
	/* 【mapping】A 管理页面确认了要添加这个slot 更新slotMenu 同时重定向更新slot controller的slot menu*/
	@RequestMapping("/ConfirmAddSlot")
	public String ConfirmAddSlot(String entityType,String entity) {
		List<String> singleSlot = slotMenu.get(entityType);
		singleSlot.add(entity);
		newSlotMenu.remove(entityType, entity);
		slotMenu.put(entityType, singleSlot);
		return "redirect:./test/还没定?entityType="+entityType+"&entity="+entity;
	}
	
	/* 【mapping】管理页面要求删除该自定义slot 然后再刷新回来*/
	@RequestMapping("/DeleteDIYSlot")
	public String DeleteDIYSlot(String entityType,String entity) {
		newSlotMenu.remove(entityType, entity);
		return "redirect:overview";
	}
	
	/* 【mapping】B 至少。。给个重复标签提示*/
	@ResponseBody
	@RequestMapping("/NewSlotDenied")
	public String NewSlotDenied(String message) {
		return message;
	}
	
	/* 【mapping】前端点了按钮“保存slotMenu”，保存到文件 */
	@ResponseBody
	@RequestMapping("/savemenu")
	public String savemenu() {
		fileUtils.writeObjectToFile(slotMenu, slotMenuFilename);
		return "保存成功";
	}
	
	/* 目前是随便找的前端显示slot menu*/
	@ResponseBody
	@RequestMapping("/showslotmenu")
	public ModelAndView showSlotMmenu() {
		System.out.println(slotMenu.toString());
		ModelAndView mav = new ModelAndView();
		mav.addObject("test4", slotMenu);
		mav.setViewName("test");
		return mav;
	}
		
	
	
	
	
	
	/* 反正又是测试 测试用的管理页面*/
	@ResponseBody
	@RequestMapping("/atest")
	public ModelAndView atest() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("aaa", "aaa");
		mav.setViewName("testManage");
		return mav;
	}
	
	
	
	
	
	
	
	//试一下不同controller之间的重定向
	@RequestMapping("/subtest")
	public String subtest() {
		int id=0;
		System.out.println("苏卡不列特");
		if(id==0) {
			return "redirect:./test/subsubTest";
		}else return "redirect:./test/hello?id="+id;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* 把这个辣眼睛的生成菜单放最下面 */
	@RequestMapping("/initmenu")
	public String initSlotValueMenu() {
		singleSlot = new ArrayList<>(Arrays.asList("鲜蔬鸡肉饭","鲜蔬鳕鱼三文鱼饭","牛霖牛肉饭","鲜蔬鳕鱼大虾饭","原切鸡肉南瓜饭",
				"原切酥骨鲅鱼饭","原切三文鱼大虾饭","鸡肉南瓜幼猫饭","鱼鱼大虾饭","酥骨鸡腿饭","冻干鸡肉饭","冻干鸭肉饭","牛肉鳕鱼饭","原切鳕鱼大虾饭",
				"鸭肉猫饭","原切兔子饭","鸡肉南瓜银鱼饭","原切三文鱼饭","鲜蔬牛肉饭","妙鲜包","小鱼干","鲜蔬南瓜饭","鸡肉鳕鱼大虾饭","单品鸡肉饭",
				"单品牛肉饭","鸡肉牛肉饭","无谷鸡肉鳕鱼大虾饭","鱼加鸡蛋低脂鳕鱼饭","虾仁饭","肥鸡饭","秋刀鱼","鲫鱼","猫草饭"));
		slotMenu.put("猫粮种类",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("异味","包装问题","变质","异物","量少"));
		slotMenu.put("投诉",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("会员","猫砂","水碗","猫窝","猫砂盆","鸡鸡牛牛饭套餐"));
		slotMenu.put("其他商品",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("北京","石家庄","成都","贵阳","海南"));
		slotMenu.put("地点",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("普快","陆运","次日"));
		slotMenu.put("快递种类",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("顺丰快递","韵达快递","天天快递","申通快递","圆通快递"));
		slotMenu.put("快递名称",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("支付宝"));
		slotMenu.put("支付方式",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("成年猫","流浪猫","英国短毛猫","加菲猫","未成年猫","金吉拉猫","母猫","斯芬克斯猫",
				"孕猫","哺乳期猫","肥胖猫"));
		slotMenu.put("猫种类",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("猫的爱称"));
		slotMenu.put("猫的爱称",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("贵","便宜"));
		slotMenu.put("价格评价",singleSlot);
		
		singleSlot = new ArrayList<>(Arrays.asList("过年之前","过年之后","下周","10天","25天","两周","每月一号","每月"));
		slotMenu.put("时间段",singleSlot);
		System.out.println("成功录入");
		
		System.out.println("写入文件");
		fileUtils.writeObjectToFile(slotMenu, slotMenuFilename);
		return "生成成功！文件名："+slotMenuFilename;
	}
}
