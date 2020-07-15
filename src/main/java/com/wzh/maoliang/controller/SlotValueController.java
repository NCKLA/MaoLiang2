package com.wzh.maoliang.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wzh.maoliang.ServiceImpl.DataServiceImpl;
import com.wzh.maoliang.ServiceImpl.OrderServiceImpl;
import com.wzh.maoliang.ServiceImpl.SlotServiceImpl;
import com.wzh.maoliang.ServiceImpl.UserServiceImpl;
import com.wzh.maoliang.Utils.ObjectFileIOUtils;
import com.wzh.maoliang.Utils.initDataDistributorUtils;
import com.wzh.maoliang.Utils.initRandomIteratorUtils;
import com.wzh.maoliang.entity.Data;
import com.wzh.maoliang.entity.Order;
import com.wzh.maoliang.entity.RandomDataDistributor;
import com.wzh.maoliang.entity.RandomDataDistributor.Element;
import com.wzh.maoliang.entity.SlotValueLabelInfo;
import com.wzh.maoliang.entity.User;

@Controller
@RequestMapping("/slot")
public class SlotValueController {

	@Resource
	private DataServiceImpl dataservice;

	@Resource
	private OrderServiceImpl orderservice;

	@Resource
	private SlotServiceImpl slotservice;

	@Resource
	private UserServiceImpl userservice;

	/* 保存题库的文件名 */
	private String filename = "testRDD";
	
	private initRandomIteratorUtils IUtils = new initRandomIteratorUtils();
	
	private ObjectFileIOUtils oUtils = new ObjectFileIOUtils();

//	private RandomDataDistributor randomDataDistributor;
	private RandomDataDistributor randomDataDistributor = (RandomDataDistributor) oUtils.readObjectFromFile(filename);

	private String slotMenuFilename = "slotValueMenu";
	
	@SuppressWarnings("unchecked")
	private Map<String, List<String>> slotMenu = (Map<String, List<String>>) oUtils
			.readObjectFromFile(slotMenuFilename);

	/* 抽题 记得想想在哪里加synchronized */
	@ResponseBody
	@RequestMapping("/draw")
//	public void draw(int userId, char hasLabeled, int hisDataId) {
	public ModelAndView draw(int userId, char hasLabeled, int hisDataId) {
		ModelAndView mav = new ModelAndView();
		Data newData = new Data();

		System.out.println("userId:" + userId + "  hasLabeled:" + hasLabeled + "  hisDataId:" + hisDataId);
		/* 抽题之前先来个判断，如果当前任务还没完成，那就带着没完成的任务去前端 */
		if (hasLabeled == 'T') {
			newData = initData4User(userId);
			System.out.println("为该用户生成的dataId：" + newData.toString());
			Data data = newData;
			/* 生成可标注的题之后，修改用户标注表项，保存dataid，并将hasLabeled置F */
			userservice.updateSlotLabelState(data.getDataId(),'F', userId);

			mav.addObject("noSuitDataId", "false");
			mav.addObject("data", data);

			/* 取得当前数组下标对应的data的order */
			mav.addObject("order", (Order) randomDataDistributor.getOrderMap().get(data.getOrderId()));

			/* 给出上下文，必要时db */
			mav.addObject("contexts", initContextAboveAndBelow(data.getDataId(), data.getOrderId()));

			/* 下拉菜单信息（供标注的标签） */
			mav.addObject("slotMenu", slotMenu);

			mav.setViewName("slotValuelabeling");
			
			mav.addObject("userId", userId);
			
			System.out.println("已抽完新数据  接下来会进行抽题后处理");
			
			/* （前提是已经抽题成功）最大标注人数-1，且判断是否为0; */
			if (randomDataDistributor.linkRemainMinusOneAndIsZero(newData.getDataId()))
				/* 这里做一个抽题之后的处理 标注人数-1 留个传参，免得忘了 */
				handleAfterRemainZero(data.getDataId(), data.getOrderId());

		} else if (hasLabeled == 'F') {
			/**
			 * 有重复代码 这块跟抽新题之后的处理没啥差别 区别是已知这人没完成任务
			 */
//			mav.addObject("noSuitDataId", "false");
			Data data = new Data();
			int pointer2 = randomDataDistributor.linkHasData(hisDataId);

			/* 去题库找，没有就db，因为这个his data id 是从db拿的 所以一定有 可以不判断有无这个id */
			if (pointer2 != -1)
				data = randomDataDistributor.linkSelect(pointer2).getData();

			data = dataservice.findById(hisDataId).get();

			mav.addObject("noSuitDataId", "false");
			mav.addObject("data", data);

			/* 取得当前数组下标对应的data的order */
			mav.addObject("order", (Order) randomDataDistributor.getOrderMap().get(data.getOrderId()));

			/* 下拉菜单信息（供标注的标签） */
			mav.addObject("slotMenu", slotMenu);
			
			mav.addObject("userId", userId);

			/* 给出上下文，必要时db */
			mav.addObject("contexts", initContextAboveAndBelow(data.getDataId(), data.getOrderId()));
			mav.setViewName("slotValuelabeling");
			System.out.println("已将尚未完成的任务信息读取");
		} else {
			System.out.println("hasLabel字段错误");
		}
		System.out.println("已获得任务信息  前往slot value标注页面");
		return mav;
	}

	/** 给用户整个题标，当OutsiderMap有题则优先从中随机，没有则从数组题库里随机 */
	public Data initData4User(int userId) {
		Map<Integer, Element> outSiderMap = randomDataDistributor.getOutSiderMap();
		/* 优先outsiderMap */
		if (!outSiderMap.isEmpty()) {
			Data newData = new Data();
			Iterator<Integer> iterator = outSiderMap.keySet().iterator();
			int temp = 0;
			while (iterator.hasNext()) {
				temp = iterator.next();
				if (!hasLabeled(temp, userId))
					break;
				temp = 0;
			}
			if (temp == 0)
				System.out.println("待标注数据生成失败");
			else
				newData = outSiderMap.get(temp).data;
			return newData;
		} else {
			/* 改了随机规则  生成0-29的乱序迭代器 */
			int temp = -1;
			Iterator<Integer> iterator = IUtils.ShuffleIteritor();
			do{
				try {
					temp = (int) iterator.next();
					System.out.println("随机抽取题库的数组下标："+temp);
				} catch (Exception e) {
					System.out.println("没有随机了，这人题库全标了一遍");
				}
			}while(hasLabeled(randomDataDistributor.linkSelect(temp).data.getDataId(), userId));
			
			return randomDataDistributor.linkSelect(temp).data;
		}
	}

	/** 给出上下文 ,只要对话信息，order id是不是一组不管，再判断都行 （20.3.13 已删除上下文map） */
	public List<String> initContextAboveAndBelow(int dataId, int orderId) {
		List<String> contexts = new ArrayList<>();
		int i;
		for (i = dataId - randomDataDistributor.getContextLength(); i <= dataId
				+ randomDataDistributor.getContextLength(); i++) {
			if (i == dataId)
				continue;
			System.out.println("获取上下文中   当前所需上下文的dataID：" + i);
			/* 先去map里找 （20.3.13 已删除）*/
			/* 再去题库里找 */
			int index =  randomDataDistributor.linkHasData(i);
			if (index > 0) {
				System.out.println("题库拿上下文");
				contexts.add(randomDataDistributor.linkSelect(index).data.getChatRecord());
				/* 最后去db找，要求同一个order id */
			} else {
				Optional<Data> optional = dataservice.findById(i);
				if (optional.isPresent()) {
					contexts.add(optional.get().getChatRecord());
					System.out.println("db拿上下文");
				} else {
					contexts.add("");
					System.out.println("拿了个寂寞");
				}
			}
		}
		return contexts;
	}

	/** 抽题之后的处理    如果剩余标注次数为0，加新题，改ordermap，或者从额外题库删除                   加锁感觉可以加在这里 （20.3.13 上下文map已删除，修改注释）*/
	public void handleAfterRemainZero(int dataId, int orderId) {

		/**
		 * 先来写一下改掉的地方： 输入没有了index，只有dataid，所以要先判断是不是数组里的内容，
		 * （多嘴一句，这里逻辑跟原来差不多，数组才用管，超出题库外的也不用管。。。也不是，要remove掉，那可以用contains key嘛）
		 * 上下文的添加：每次在数组题库加新数据，抽取题号+2（上下文长度）的题 
		 * 上下文的删除：当且仅当该dataid为数组内最小值，删除 <dataid-1  的所有上下文 
		 * ordermap的更改：接口功能没变，但是以前遍历是走指针的，得改改方法内容
		 * 最后加新题：直接覆盖了，也不存在什么指针下移
		 */

		if (randomDataDistributor.linkDeleteAndIsInsider(dataId)) {
			int index = randomDataDistributor.linkHasData(dataId);
			int newDataId = randomDataDistributor.getCurrentDataId();
			Optional<Data> newDataOp = dataservice.findById(newDataId);
			randomDataDistributor.setCurrentDataId(++newDataId);

			/* 改上下文 （20.3.13 已删除） */			
			/* 再按照注释里面的规则 （20.3.13 已删除）*/
			/* 改order */
			Map<Integer, Order> orderMap = randomDataDistributor.getOrderMap();
			
			/* 先看 除了这份被删除的数据之外 还有没有其他data订单编号与它相同 如果没有 删掉 */			
			if (!OtherDataHasSameOrder(index, orderId)) { 
				orderMap.remove(orderId);
				randomDataDistributor.setOrderMap(orderMap); 
			}			 

			/* 再看 新加的数据order有没有 如果没有 db */
			if (!orderMap.containsKey(newDataOp.get().getOrderId())) {
				Optional<Order> orderOp = orderservice.findById(newDataOp.get().getOrderId());
				if (orderOp.isPresent())
					orderMap.put(orderOp.get().getOrderId(), orderOp.get());
			}

			/* 加新题，指针bu会自动下移 */
			randomDataDistributor.linkAdd(newDataOp.get(), index);
		}else if(randomDataDistributor.getOutSiderMap().containsKey(dataId)) {
			Map<Integer, Element> outsiderMap = randomDataDistributor.getOutSiderMap();
			outsiderMap.remove(dataId);
			randomDataDistributor.setOutSiderMap(outsiderMap);
		}
	}

	
	/* 除了这份被删除的数据之外 还有没有其他data订单编号与它相同 */
	public boolean OtherDataHasSameOrder(int index, int OrderId) { 
		for (int i = 0;i<randomDataDistributor.getUseSize();i++) { 
			if(i == index) continue;
			if(randomDataDistributor.linkSelect(i).data.getOrderId() == OrderId)
				return true; 
			} 
		return false; 
	}
	 

	/* Boolean db判断该用户是否标过此题（用户id，data_id） */
	public boolean hasLabeled(int dataId, int userId) {
		Optional<SlotValueLabelInfo> optional = Optional.empty();
		optional = slotservice.findByDataIdAndUserId(dataId, userId);
		if (optional.isPresent())
			return true;
		else
			return false;
	}

//	【mapping】保存标注数据进db,并修改用户的hasLabeled表项，目前这个页面只处理slot
	/*
	 * 把前端传入的数据存入DB user的标注项（当前数据id，是否已完成当前工作）的修改
	 */

//	【mapping】前端点了跳过回来

//	【mapping】从选择任务界面进到这里，从session获取user后重定向到抽题draw
	@RequestMapping("/gotoDraw")
	public String gotoDraw(HttpServletRequest request) {
		User us = (User) request.getSession().getAttribute("user");
		Optional<User> optional = userservice.findById(us.getUserId());
		User u = optional.get();
		/* int userId, boolean hasLabeled, int hisDataId */
		return "redirect:draw?userId=" + u.getUserId() + "&hasLabeled=" + u.getSlotHasDoneCurrentData() + "&hisDataId="
				+ u.getSlotCurrentDataId();
	}

//	【mapping】前端修改历史标注（可以暂时不写）

//	【mapping】保存为文件，设计是从management的controller调用
	@ResponseBody
	@RequestMapping("/savefile")
	public String saveFile() {
		System.out.println("到了保存文件的后台  准备开始存题库和slot value menu");
		oUtils.writeObjectToFile(randomDataDistributor, filename);
		oUtils.writeObjectToFile(slotMenu, slotMenuFilename);
		return "save file success";
	}

//	【mapping】management页面slotMenu添加了新的，重定向之后也给这里添加  然后再重定向回到management页面

//	【mapping】传到管理页面的数据，ajax
	@ResponseBody
	@RequestMapping(value = "/ajaxToManage", method = RequestMethod.POST)
	public HashMap<Object,Object> ajaxToManage() {
		HashMap<Object,Object> map = new HashMap<Object, Object>();
		map.put("dataDistributor", randomDataDistributor);
		map.put("maxRemain", randomDataDistributor.getMaxRemain());
		map.put("filename", filename);
		return map;
	}

//	【mapping】接收管理页面传参，并修改相应参数
	@ResponseBody
	@RequestMapping("/dataFromManage")
	public String dataFromManage(@RequestParam("p2") Integer manRemain, @RequestParam("p1") String fileName) {
		System.out.println("管理页面传参到了后台");
		System.out.println("manRemain：" + manRemain + "   fileName:" + fileName);
		filename = fileName;
		randomDataDistributor.linkAlterMaxRemain(manRemain);
		return "成功接收";
	}
	
	@ResponseBody
	@PostMapping("/receive")
	public String receive(@RequestParam Map<String,Object> params,HttpServletResponse response) {	
		System.out.println("提交到后台，打印信息");
		System.out.println("map:   "+params.toString());
		System.out.println("keyset:"+params.keySet().toString());
		

		
		if(params.containsKey("dataId") && params.containsKey("userId") && 
				params.containsKey("submitType")) {
			
			int userId = Integer.valueOf((String) params.get("userId"));
			int dataId = Integer.valueOf((String) params.get("dataId"));
			char submitType = params.get("submitType").equals("L")?'L':'S';
			
			System.out.println("基础信息都有，开始遍历下拉列表");
			
//			如果是skip 跳过  那就直接存进db  同时修改user的标注信息
			if('S'==submitType) {
				slotservice.save(new SlotValueLabelInfo(userId,dataId,submitType,"NULL"));
			}
			
//			如果标注的是无slot 即第一个下拉菜单都为空
			else if("NULL".equals(params.get("selectA")) && "NULL".equals(params.get("selectB"))) {
				slotservice.save(new SlotValueLabelInfo(userId,dataId,submitType,"NULL"));
				System.out.println("下拉第一项为NULL");
//			这两种都不是，假设已经通过前端的验证。。。或者说加个后台验证
			}else {
//			多值最好用json，这里用的是google的gson
//			计划是把标注信息先弄成map，再通过map转json
				Map<String,List<String>> slotmap = new HashMap<String,List<String>>();
				
				Iterator<String> ite = params.keySet().iterator();
				String ss = new String();
				while(ite.hasNext()) {
					ss = ite.next();
					if(ss.indexOf("selectA") > -1) {
						if(!slotmap.containsKey((String) params.get(ss))) slotmap.put((String) params.get(ss), new ArrayList<String>());
							
						slotmap.get((String) params.get(ss)).add((String) params.get(ss.replace('A', 'B')));
					}
				}
				
				JSONObject jsonObj=new JSONObject(slotmap);
				System.out.println("这个json准备送到数据库了"+jsonObj.toString());
				slotservice.save(new SlotValueLabelInfo(userId,dataId,submitType,jsonObj.toString()));
			}
			
//			不管是哪一个if里走出来的  最后都要修改用户信息
			userservice.updateSlotLabelState(dataId, 'T', userId);
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json;charset=utf-8");
			
			return "<script>alert('submit success!preparing for next data');location='"
			+ "draw?userId=" + userId
			+ "&hasLabeled=" + 'T'
			+ "&hisDataId=" + dataId
			+ "';</script>";
			
		}else {
			System.out.println("缺少data或userid,或第一条下拉列表,或者submitType");
			return "ERROR，please turn back";
		}
			
//		return "可以返回了";
//		return "<script>alert('提交成功，准备下一组数据。');location="
//				+ "redirect:draw?userId="
//				+ "&hasLabeled=" 
//				+ "&hisDataId="
//				+ "</script>";
//		return "redirect:draw?userId=" + u.getUserId() + "&hasLabeled=" + u.getSlotHasDoneCurrentData() + "&hisDataId="
//		+ u.getSlotCurrentDataId();
	}
	
	
	
	
//	下面这块写着测试玩的
	@RequestMapping("/go")
	public String goWebPage() {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("test2");
		return "redirect:../src/main/resources/webapp/test2.ftl";
	}

	@ResponseBody
	@RequestMapping("/testpage")
	public ModelAndView nidaye() {
		ModelAndView mav = new ModelAndView();
//		mav.addObject("test1", dataService.findFirstDataId());
		mav.addObject("test1", randomDataDistributor.toString());
		mav.setViewName("test");
		return mav;
	}

	@ResponseBody
	@RequestMapping("/initDD")
	public ModelAndView initDD() {
		initDataDistributorUtils util = new initDataDistributorUtils();
		util.setDataService(dataservice);
		util.setOrderservice(orderservice);
		util.writeRDDFile(filename);
		return null;
	}

	/* 这就是测试重定向吗，真是有够好笑的呢^^ _ */
	@RequestMapping("/subtest")
	public String subTest(RedirectAttributes arr) {
		String key = "food";
		String value = "hunberger";
		System.out.println("苏卡不列特");
		return "redirect:hello?key=" + key + "&value=" + value;
	}

	@RequestMapping("/subsubTest")
	@ResponseBody
	public String subsubTest() {
		return "添加成功！";
	}
//	int tempint = 1;

	@RequestMapping("/hello")
	@ResponseBody
//	public String call(){
	public String call(String key, String value) {
		return "just a test,key：" + key + "  value：" + value;
	}
}
