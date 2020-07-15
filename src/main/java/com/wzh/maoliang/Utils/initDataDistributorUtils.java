package com.wzh.maoliang.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wzh.maoliang.ServiceImpl.DataServiceImpl;
import com.wzh.maoliang.ServiceImpl.OrderServiceImpl;
import com.wzh.maoliang.entity.Data;
import com.wzh.maoliang.entity.DataDistributor;
import com.wzh.maoliang.entity.Order;
import com.wzh.maoliang.entity.RandomDataDistributor;

@Component
public class initDataDistributorUtils {
	
	public DataServiceImpl getDataService() {
		return dataService;
	}

	public void setDataService(DataServiceImpl dataService) {
		this.dataService = dataService;
	}

	public OrderServiceImpl getOrderservice() {
		return orderservice;
	}

	public void setOrderservice(OrderServiceImpl orderservice) {
		this.orderservice = orderservice;
	}
	
	@Resource
	private  DataServiceImpl dataService;
	
	@Resource
	private  OrderServiceImpl orderservice;
	
	private ObjectFileIOUtils oUtils = new ObjectFileIOUtils();

	public DataDistributor init(){ 
		return new DataDistributor(initDDData(),initDDOrder());	
	}
	
	public RandomDataDistributor initRDD(){ 
		return new RandomDataDistributor(initDDData(),initDDOrder());	
	}
	
	public  List<Data> initDDData() {
		List<Data> datas = new ArrayList<Data>();
		/* 先获取最小的id编号 */
		int minDataId = dataService.findFirstDataId();
		/* 然后循环30次（题库容量）并存入list */
		if(dataService==(null)) System.out.println("service是他吗的null！");
		Optional<Data> dataOp;
		for(int i = 0;i<30;i++) {
			dataOp = dataService.findById(minDataId++);
//			System.out.println("dataOp："+dataOp.toString());
			if(dataOp.isPresent()) datas.add(dataOp.get());
			else --i;
		}
		
		return datas;	
	}
	public  Map<Integer,Order> initDDOrder(){
		/* 先拿data的信息 */
		List<Data> firstTimeDatas = initDDData();
		Map<Integer,Order> orderMap = new HashMap<Integer, Order>();
		int currentOrderId = -1;
		/* 遍历，当对话的订单编号变化，则db抽新的订单上来,并放进map*/
		for(Data tempData:firstTimeDatas) {
			if(currentOrderId!=tempData.getOrderId()) {
				currentOrderId=tempData.getOrderId();
				orderMap.put(currentOrderId, orderservice.findById(currentOrderId).get());
			}
		}
		return orderMap;
	}

	/* 新题库的设计需要新的上下文map （20.3.14 已删除）*/
	
	public  void writeFile(String fileName) {
		oUtils.writeObjectToFile(init(),fileName);
	}
	public  void writeRDDFile(String fileName) {
		oUtils.writeObjectToFile(initRDD(),fileName);
	}
}
