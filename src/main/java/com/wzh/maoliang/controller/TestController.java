package com.wzh.maoliang.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
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
import com.wzh.maoliang.entity.Data;
import com.wzh.maoliang.entity.DataDistributor;
import com.wzh.maoliang.entity.Order;
import com.wzh.maoliang.entity.SlotValueLabelInfo;
import com.wzh.maoliang.entity.User;

/**
 * 用于给新写的controller测试，现在暂时是slot标注相关
 * 
 * @author G50
 *
 */
@Controller
@RequestMapping("/test")
public class TestController {

	@Resource
	private DataServiceImpl dataservice;

	@Resource
	private OrderServiceImpl orderservice;

	@Resource
	private SlotServiceImpl slotservice;

	@Resource
	private UserServiceImpl userservice;

	/* 保存题库的文件名 */
	private String filename = "testDD";

	private ObjectFileIOUtils oUtils = new ObjectFileIOUtils();

//	private DataDistributor dataDistributor;
	private DataDistributor dataDistributor = (DataDistributor) oUtils.readObjectFromFile(filename);

	private String slotMenuFilename = "slotValueMenu";
	@SuppressWarnings("unchecked")
	private Map<String, List<String>> slotMenu = (Map<String, List<String>>) oUtils
			.readObjectFromFile(slotMenuFilename);

	/* 抽题 记得想想在哪里加synchronized */
//	@ResponseBody
	@RequestMapping("/draw")
	public void draw(int userId, char hasLabeled, int hisDataId) {
//	public ModelAndView draw(int userId, char hasLabeled, int hisDataId) {
		ModelAndView mav = new ModelAndView();

		System.out.println("userId:" + userId + "  hasLabeled:" + hasLabeled + "  hisDataId:" + hisDataId);
		/* 抽题之前先来个判断，如果当前任务还没完成，那就带着没完成的任务去前端 */
		if (hasLabeled=='T') {
			int pointer = initDataIndex4User(userId);
			System.out.println("生成的该用户数组下标：" + pointer);
			if (pointer == -1)
				mav.addObject("noSuitDataId", "true");
			else {
				Data data = dataDistributor.linkSelect(pointer).data;
				/* 生成可标注的题之后，修改用户标注表项，保存dataid，并将hasLabeled置F */
				userservice.updateSlotLabelState(data.getDataId(),'F' ,userId);

				mav.addObject("noSuitDataId", "false");
				mav.addObject("data", data);

				/* 取得当前数组下标对应的data的order */
				mav.addObject("order", (Order) dataDistributor.getOrderMap().get(data.getOrderId()));

				/* 给出上下文，必要时db */
				mav.addObject("contexts", initContextAboveAndBelow(data.getDataId(), data.getOrderId()));

				/* 下拉菜单信息（供标注的标签） */
				mav.addObject("slotMenu", slotMenu);

				mav.setViewName("test3");
				/* （前提是已经抽题成功）最大标注人数-1，且判断是否为0; */
				if (dataDistributor.linkRemainMinusOneAndIsZero(pointer))
					/* 这里做一个抽题之后的处理 标注人数-1 留个传参，免得忘了 */
					handleAfterRemainZero(pointer, data.getDataId(), data.getOrderId());

			}
		} else if (hasLabeled=='F'){
			/**
			 * 有重复代码 这块跟抽新题之后的处理没啥差别 区别是已知这人没完成任务
			 */
			mav.addObject("noSuitDataId", "false");
			Data data = new Data();
			int pointer2 = dataDistributor.linkHasData(hisDataId);

			/* 去题库找，没有就db，因为这个his data id 是从db拿的 所以一定有 可以不判断有无这个id */
			if (pointer2 != -1)
				data = dataDistributor.linkSelect(pointer2).getData();
			else
				data = dataservice.findById(hisDataId).get();

			mav.addObject("noSuitDataId", "false");
			mav.addObject("data", data);

			/* 取得当前数组下标对应的data的order */
			mav.addObject("order", (Order) dataDistributor.getOrderMap().get(data.getOrderId()));

			/* 下拉菜单信息（供标注的标签） */
			mav.addObject("slotMenu", slotMenu);

			/* 给出上下文，必要时db */
			mav.addObject("contexts", initContextAboveAndBelow(data.getDataId(), data.getOrderId()));
			mav.setViewName("test3");
		}else {
			System.out.println("hasLabel字段错误");
		}

//		return mav;
	}

	/* 给用户整个题标，一旦发现有题直接返回data index，实在没得标那就给个-1 */
	public int initDataIndex4User(int userId) {
		int pointer = dataDistributor.getFirst();
		if (!hasLabeled(pointer, userId))
			return pointer;
		for (pointer = dataDistributor.linkSelect(pointer).next; pointer != dataDistributor
				.getFirst(); pointer = dataDistributor.linkSelect(pointer).next) {
			if (!hasLabeled(pointer, userId))
				return pointer;
		}
		return -1;
	}

	/* 给出上下文 ,只要文，order id是不是一组不管，再判断都行 */
	public List<String> initContextAboveAndBelow(int dataId, int orderId) {
		List<String> contexts = new ArrayList<>();
		Map<Integer, Data> map = dataDistributor.getContextMap();
		int i;
		for (i = dataId - dataDistributor.getContextLength(); i <= dataId + dataDistributor.getContextLength(); i++) {
			if (i == dataId)
				continue;
			System.out.println("当前dataID：" + i);
			/* 先去map里找 */
			if (map.containsKey(i)) {
				System.out.println("map拿上下文");
				contexts.add(map.get(i).getChatRecord());
				/* 再去题库里找 */
			} else if (dataDistributor.linkHasData(i) > 0) {
				System.out.println("题库拿上下文");
				contexts.add(dataDistributor.linkSelect(i).data.getChatRecord());
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

	/* 抽题之后的处理，加新题，改order，改上下文，改user的session 加锁感觉可以加在这里 */
	public void handleAfterRemainZero(int index, int dataId, int orderId) {
		if (dataDistributor.linkDeleteAndIsInsider(index)) {
			int newDataId = dataDistributor.getCurrentDataId();
			Optional<Data> newDataOp = dataservice.findById(dataDistributor.getCurrentDataId());
			dataDistributor.setCurrentDataId(++newDataId);

			/* 改上下文 */
			Map<Integer, Data> contextMap = dataDistributor.getContextMap();
			if (contextMap.containsKey(dataId - dataDistributor.getContextLength())) {
				contextMap.remove(dataId - dataDistributor.getContextLength());
				contextMap.put(dataId, dataDistributor.linkSelect(index).data);
				dataDistributor.setContextMap(contextMap);
			}
			/* 改order */
			Map<Integer, Order> orderMap = dataDistributor.getOrderMap();
			/* 先看 除了这份被删除的数据之外 还有没有其他data订单编号与它相同 如果没有 删掉 */
			if (!OtherDataHasSameOrder(index, orderId)) {
				orderMap.remove(orderId);
				dataDistributor.setOrderMap(orderMap);
			}

			/* 再看 新加的数据order有没有 如果没有 db */
			if (!orderMap.containsKey(newDataOp.get().getOrderId())) {
				Optional<Order> orderOp = orderservice.findById(newDataOp.get().getOrderId());
				if (orderOp.isPresent())
					orderMap.put(orderOp.get().getOrderId(), orderOp.get());
			}

			/* 加新题，指针会自动下移 */
			dataDistributor.linkAdd(newDataOp.get());
		}
	}

	/* 除了这份被删除的数据之外 还有没有其他data订单编号与它相同 */
	public boolean OtherDataHasSameOrder(int index, int OrderId) {
		for (int pointer = dataDistributor.linkSelect(index).next; dataDistributor
				.linkSelect(pointer).next != index; pointer = dataDistributor.linkSelect(pointer).next) {
			if (dataDistributor.linkSelect(pointer).data.getOrderId() == OrderId)
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
		User u = (User)request.getSession().getAttribute("user");
		/* int userId, boolean hasLabeled, int hisDataId */
		return "redirect:draw?userId="+u.getUserId()+"&hasLabeled="+u.getSlotHasDoneCurrentData()+"&hisDataId="+u.getSlotCurrentDataId();
	}
	
	
	
//	【mapping】前端修改历史标注（可以暂时不写）
	
	

//	【mapping】保存为文件，设计是从management的controller调用
	@ResponseBody
	@RequestMapping("/savefile")
	public String saveFile() {
		System.out.println("到了保存文件的后台");
		oUtils.writeObjectToFile(dataDistributor, filename);
		oUtils.writeObjectToFile(slotMenu, slotMenuFilename);
		return "save file success";
	}

//	【mapping】management页面slotMenu添加了新的，重定向之后也给这里添加  然后再重定向回到management页面
	
	
	
//	【mapping】传到管理页面的数据，ajax
	@ResponseBody
	@RequestMapping(value = "/ajaxToManage", method = RequestMethod.POST)
	public HashMap<Object,Object> ajaxToManage() {
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("dataDistributor", dataDistributor);
		map.put("maxRemain", dataDistributor.getMaxRemain());
		map.put("filename", filename);
		return map;
	}
	
//	【mapping】接收管理页面传参，并修改相应参数
	@ResponseBody
	@RequestMapping("/dataFromManage")
	public String dataFromManage(@RequestParam("p2") Integer manRemain,@RequestParam("p1")String fileName) {
		System.out.println("传参到了后台");
		System.out.println("manRemain："+manRemain+"   fileName:"+fileName);
		filename = fileName;
		dataDistributor.linkAlterMaxRemain(manRemain);
		return "成功接收";
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
		mav.addObject("test1", dataDistributor.toString());
		mav.setViewName("test");
		return mav;
	}

	@ResponseBody
	@RequestMapping("/initDD")
	public ModelAndView initDD() {
		initDataDistributorUtils util = new initDataDistributorUtils();
//		util.setDataService(dataservice);
//		util.setOrderservice(orderservice);
		util.writeFile(filename);
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
