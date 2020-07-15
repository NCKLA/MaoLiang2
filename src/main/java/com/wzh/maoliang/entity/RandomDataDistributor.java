package com.wzh.maoliang.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomDataDistributor implements Serializable{

	public Element[] getLinkList() {
		return linkList;
	}

	@Override
	public String toString() {
		return "RandomDataDistributor [linkList=" + Arrays.toString(linkList) + ", DEFAULT_SIZE=" + DEFAULT_SIZE
				+ ", first=" + first + ", last=" + last + ", useSize=" + useSize + ", outSider=" + outSider
				+ ", maxRemain=" + maxRemain + ", contextLength=" + contextLength + ", currentDataId=" + currentDataId
				+ ", orderMap=" + orderMap + ", OutSiderMap=" + OutSiderMap + "]";
	}
	
	public class Element implements Serializable{
		private static final long serialVersionUID = 1L;
		public Data data;
    	public int remain;//剩余可标注人数
		/*
		 * public int next = -1;//下一个结点的数组下标   随机抽题不需要这个东西
		 */        
    	public Element(Data data,int remain){
        	this.data = data;
        	this.remain = remain;
        }
		public Element(){}
		public Data getData() {
			return data;
		}
		public void setData(Data data) {
			this.data = data;
		}
		public int getRemain() {
			return remain;
		}
		public void setRemain(int remain) {
			this.remain = remain;
		}
		@Override
		public String toString() {
			return "Element [data=" + data + ", remain=" + remain + "]";
		}
    }
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Element[] linkList; 
    private int DEFAULT_SIZE = 100;//默认存储大小
    private int first;//指向链当前位置
    private int last;//指向链表可插入的最后一项(暂时没用)
    private int useSize;//题库容量
    private int outSider;//题库容量
    private int maxRemain;//最大标注人数
    private int contextLength;//上文/下文数量    
//    （20.3.13 已删除）上下文map
    
	/*
     * 以下是考虑到controller层不方便初始化，放到这里的变量，尽量减少与原变量的互动
     */
    private int currentDataId;//如果要抽取新数据，新数据的id，抽完之后+1+1
    private Map<Integer,Order> orderMap;//订单map
	

	public RandomDataDistributor(List<Data> datas,Map<Integer,Order> orders){
		System.out.println("准备生成Random题库");
    	setLinkList(new Element[DEFAULT_SIZE]);
    	setFirst(0);
    	setLast(0);
    	setContextLength(2);
    	setUseSize(datas.size());
    	setOutSider(getUseSize());
    	setMaxRemain(15);
    	System.out.println("送到题库的订单orderMap："+orders.toString());
    	System.out.println("准备生成订单map 主数组");
    	setOrderMap(orders);
    	/* 数据都给了，这是真的初始化，包括上下文和订单 */
    	setCurrentDataId(datas.get(datas.size()-1).getDataId()+1);
    	for(int i = 0;i<useSize;i++) linkList[i] = new Element(datas.get(i),maxRemain);
    	
    	System.out.println("Random题库生成完毕！");
	}
	
	public RandomDataDistributor() {}
	
	/**
	 * 以上是不变的部分，基本上该有的都得有
	 * 多的属性：outsiderMap  题库外的退题回来
	 * 其余方法跟着内容  需要db的留好接口等controller传参
	 */	
	private Map<Integer,Element> OutSiderMap = new HashMap<Integer,Element>();
	
	/** 增,新的data信息，用于DB给的新数据  */
	public void linkAdd(Data newData,int index) {
		this.linkList[index].data = newData;
		this.linkList[index].remain = maxRemain;
	}

	/** 增，用于跳过的，设定最大标注数量 */
	public void linkAddOutSide(Data newData) {
		int index = linkHasData(newData.getDataId());
    	if(index!=-1) {
    		this.linkList[index].remain++;
    	}else if(OutSiderMap.containsKey(newData.getDataId())){
    		Element e1 = OutSiderMap.get(newData.getDataId());
    		e1.remain++;
    		OutSiderMap.replace(newData.getDataId(),  OutSiderMap.get(newData.getDataId()), e1);
    	}else System.out.println("跳过的题添加失败");
    	
	}

	/** 删,并返回data信息  是否位数组内的 */
	public boolean linkDeleteAndIsInsider(int dataId) {
		int pointer = linkHasData(dataId);
    	if(pointer ==-1) {
    		OutSiderMap.remove(dataId);
    		return false;
    	}else if(0 < pointer  &&  pointer < useSize) return true;    	
    	System.out.println("删除的节点有误");
		return false;
	}

	/** 判断数组题库中有无id为此的数据，有则返回数组下标，无则返回-1 */
    public int linkHasData(int dataId) {
    	int temp = -1;
    	for(int i = 0;i<useSize;i++) {
    		if(this.linkList[i].data.getDataId()==dataId) temp = i;
    	}
    	return temp;
    } 

	public void linkAlterData(int index,Data data) {
		this.linkList[index].data = data;
	}
	public void linkAlterRemain(int index,int remain) {
		this.linkList[index].remain = remain;
	}
	
	/** 修改最大标注人数,要和原人数作差  20.3.13 修改 */
	public void linkAlterMaxRemain(int max) {
		if(this.maxRemain<=max) for(int i=0;i<this.useSize;i++) linkList[i].remain += (max-this.maxRemain);
		
		else {
			for(int i=0;i<this.useSize;i++) 
				if(linkList[i].remain>=(max-this.maxRemain)) linkList[i].remain -= (this.maxRemain-max);			
		}
	}
	
	/** 查(主要用于抽题时候给信息) 因为linklist是private的 外部访问得通过方法  */
	public Element linkSelect(int index){
		return this.linkList[index];
	}
	
	/** 当前数组下标的题标注人数-1，且判断标注人数是否为0 */
	public boolean linkRemainMinusOneAndIsZero(int dataId) {
		int pointer=linkHasData(dataId);
		if(pointer!=-1) {
			if((--this.linkList[pointer].remain)<=0) return true;
			else return false;	
			
		}else if(OutSiderMap.containsKey(dataId)) {			
			Element e1 = OutSiderMap.get(dataId);
    		e1.remain--;
    		if(e1.remain<=0) {
    			return true;
    		}
    		else {
    			OutSiderMap.replace(dataId,  OutSiderMap.get(dataId), e1);
    			return false;
    		}
		}
		System.out.println("当前id有误！");
		return true;
	}
	
	/** 判断这个dataId是不是这个数组题库里最小的 */
	public boolean IsMinimunDataID(int dataId) {
		for(int i=0;i<useSize;i++) {
			if(linkList[i].data.getDataId()<dataId) return false;
		}
		return true;
	}
	
	public void setLinkList(Element[] linkList) {
		this.linkList = linkList;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

	public int getUseSize() {
		return useSize;
	}

	public void setUseSize(int useSize) {
		this.useSize = useSize;
	}

	public int getOutSider() {
		return outSider;
	}

	public void setOutSider(int outSider) {
		this.outSider = outSider;
	}

	public int getMaxRemain() {
		return maxRemain;
	}

	public void setMaxRemain(int maxRemain) {
		this.maxRemain = maxRemain;
	}

	public int getContextLength() {
		return contextLength;
	}

	public void setContextLength(int contextLength) {
		this.contextLength = contextLength;
	}

	public int getCurrentDataId() {
		return currentDataId;
	}

	public void setCurrentDataId(int currentDataId) {
		this.currentDataId = currentDataId;
	}

	public Map<Integer, Order> getOrderMap() {
		return orderMap;
	}

	public void setOrderMap(Map<Integer, Order> orderMap) {
		this.orderMap = orderMap;
	}
	public int getDEFAULT_SIZE() {
		return DEFAULT_SIZE;
	}
	public void setDEFAULT_SIZE(int dEFAULT_SIZE) {
		DEFAULT_SIZE = dEFAULT_SIZE;
	}
	public Map<Integer, Element> getOutSiderMap() {
		return OutSiderMap;
	}
	public void setOutSiderMap(Map<Integer, Element> outSiderMap) {
		OutSiderMap = outSiderMap;
	}
	
}
