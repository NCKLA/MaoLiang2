package com.wzh.maoliang.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 因为静态链表实质上是一维数组的存储方式，所以它在物理位置上的存储
 * 是顺序的，但它是用游标来指向下一个数据的，所以根据它的下标来获取数据
 * 和按照游标的指向来获取数据是不同的，这里设置该链表的头结点为空
 * @author 不是我
 *
 */
public class DataDistributor implements Serializable{

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
    private int contextLength;//1上文/下文数量
    
	/*
     * 以下是考虑到controller层不方便初始化，放到这里的变量，尽量减少与原变量的互动
     */
    private int currentDataId;//如果要抽取新数据，新数据的id，抽完之后+1+1
    private Map<Integer,Order> orderMap;//订单map
    private Map<Integer,Data> contextMap = new HashMap<Integer,Data>();//存上文的
    



    
    
    public class Element implements Serializable{
		private static final long serialVersionUID = 1L;
		public Data data;
    	public int remain;//剩余可标注人数
    	public int next = -1;//下一个结点的数组下标
        public Element(Data data,int remain,int next){
        	this.data = data;
        	this.remain = remain;
        	this.next = next;
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
		public int getNext() {
			return next;
		}
		public void setNext(int next) {
			this.next = next;
		}
		@Override
		public String toString() {
			return "Element [data=" + data + ", remain=" + remain + ", next=" + next + "]";
		}
    }
    
	/* 构造器 传入data和order创建 （第一次生成使用，后面都是文件读取了） */
    public DataDistributor(List<Data> datas,Map<Integer,Order> orders){
    	System.out.println("准备生成题库");
    	setLinkList(new Element[DEFAULT_SIZE]);
    	setFirst(0);
    	setLast(0);
    	setUseSize(datas.size());
    	setOutSider(getUseSize());
    	setMaxRemain(15);
    	setContextLength(2);
    	System.out.println("送到题库的订单orderMap："+orders.toString());
    	System.out.println("准备生成订单map 链表 上下文");
    	setOrderMap(orders);
    	/* 数据都给了，这是真的初始化，包括上下文和订单 */
    	setCurrentDataId( datas.get(datas.size()-1).getDataId()+1 );
    	for(int i = 0;i<useSize;) linkList[i] = new Element(datas.get(i),maxRemain,++i%useSize);    	
    	
    	for(int i = 0;i<=contextLength;i++)  contextMap.put(datas.get(i).getDataId(), datas.get(i));
    	System.out.println("题库生成完毕！");
    }
    
    public DataDistributor(){}
    
    
    
   /* 增,新的data信息，用于DB给的新数据
    * 默认顺序增加，即从front所指的位置添加
    */
    public void linkAdd(Data newData){    	
    	if(this.linkList[this.first].next==-1) {
    		this.linkList[this.first] = new Element(newData,this.maxRemain,(this.first+1)%this.useSize);
    		this.first = ++this.first%this.useSize;
    	}
    	else {
    		this.linkList[this.first] = new Element(newData,this.maxRemain,this.linkList[this.first].next);
    		this.first = this.linkList[this.first].next;
    	}
    }
    
	/* 增，用于跳过的，设定最大标注数量 */
    public void linkAddOutSide(Data newData){
		/* 判断是否越界了，这里要给个提示 */
    	
		/* 如果题库中有此题，则可标注人数+1，没有则在可用范围外新增 */
    	int index = linkHasData(newData.getDataId());
    	if(index!=-1) {
    		this.linkList[index].remain++;
    	}else{
        	this.linkList[this.outSider] = new Element(newData,1,this.linkList[this.first].next);
        	this.linkList[this.first].next = this.outSider++;
    	}
    }

	/* 删,并返回data信息 */
    public boolean linkDeleteAndIsInsider(int index){
    	if(index>=this.useSize) {
    		this.linkList[linkFrontNode(index)].next = this.linkList[index].next;
    		this.first = this.linkList[index].next;
    		this.linkList[index].next = -1;
    		this.outSider--;
    		return false;
    	}
    	else return true;
    }
    
	/* 判断题库中有无id为此的数据，有则返回数组下标，无则返回-1 */
    public int linkHasData(int dataId) {
    	int temp = -1;
    	for(int i = 0;i<outSider;i++) {
    		if(this.linkList[i].data.getDataId()==dataId) temp = i;
    	}
    	return temp;
    }    
    
	/* 修改最大标注人数,要和原人数作差 */
    public void linkAlterMaxRemain(int max) {
    	maxRemain = max;
    	int logCurrentRemain = this.linkList[this.first].remain;
    	for(int i=0;i<this.useSize;i++) linkList[i].remain = max;
    	if (logCurrentRemain>max)	this.linkList[this.first].remain = max;
    }
    
	/* 返回这个节点的前置节点index */
    public int linkFrontNode(int index) {
    	int i = 0;
    	for(;this.linkList[i].next!=index;) i = this.linkList[i].next;
    	return i;
    }
    
	/* 查(主要用于抽题时候给信息)（我觉得好像已经没啥意义了）(您好，有的，给别的页面调用)  */
	  public Element linkSelect(int index){
	  	return this.linkList[index];
	  }

	/* 修改，给数组下标，链表有三个变量，分三个吧（我觉得好像已经没啥意义了）(您好，有的，给别的页面调用) */
	public void linkAlterData(int index,Data data) {
		this.linkList[index].data = data;
	}
	public void linkAlterNext(int index,int next) {
		this.linkList[index].next = next;
	}
	public void linkAlterRemain(int index,int remain) {
		this.linkList[index].remain = remain;
	}
	
	/* 当前数组下标的题标注人数-1，且判断标注人数是否为0 */
	public boolean linkRemainMinusOneAndIsZero(int index) {
		if((--this.linkList[index].remain)<=0) return true;
		else return false;
	}
    
    
    


//  修改链表的使用大小（太麻烦，不给改，还没用）



    
	public Element[] getLinkList() {
		return linkList;
	}

	public void setLinkList(Element[] linkList) {
		this.linkList = linkList;
	}

	public int getDEFAULT_SIZE() {
		return DEFAULT_SIZE;
	}

	public void setDEFAULT_SIZE(int dEFAULT_SIZE) {
		DEFAULT_SIZE = dEFAULT_SIZE;
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
	public int getMaxRemain() {
		return maxRemain;
	}
	public void setMaxRemain(int maxRemain) {
		this.maxRemain = maxRemain;
	}

	public int getOutSider() {
		return outSider;
	}

	public void setOutSider(int outSider) {
		this.outSider = outSider;
	}
	
	public Map<Integer, Order> getOrderMap() {
		return orderMap;
	}

	public  void setOrderMap(Map<Integer, Order> orderMap) {
		this.orderMap = orderMap;
	}

	public  Map<Integer,Data> getContextMap() {
		return contextMap;
	}

	public  void setContextMap(Map<Integer,Data> contextMap) {
		this.contextMap = contextMap;
	}

	public int getCurrentDataId() {
		return currentDataId;
	}

	public void setCurrentDataId(int currentDataId) {
		this.currentDataId = currentDataId;
	}
    public int getContextLength() {
		return contextLength;
	}
	public void setContextLength(int contextLength) {
		this.contextLength = contextLength;
	}
	@Override
	public String toString() {
		return "DataDistributor [linkList=" + Arrays.toString(linkList) + ", DEFAULT_SIZE=" + DEFAULT_SIZE + ", first="
				+ first + ", last=" + last + ", useSize=" + useSize + ", outSider=" + outSider + ", maxRemain="
				+ maxRemain + ", contextLength=" + contextLength + ", getOrderMap()=" + getOrderMap()
				+ ", getContextMap()=" + getContextMap() + ", getCurrentDataId()=" + getCurrentDataId() + "]";
	}
	

    /*
     * 如果想写随机出题的添加方法 注意只需要每次判断是否为空即可 根本不需要什么弱智链表 数组都行
     */
    
/*      写到一半发现逻辑都没搞清楚  注了
 *      public void linkAddDB(Data newData,int index){
    	Element currentElement = new Element();
    	currentElement = linkSelect(index);
//    	从0开始启动
    	if(currentElement.next==-1) {
        	Element newElement = new Element(newData,this.maxRemain,++this.last);    	
        	this.linkList[index] = newElement;
    	}else {
    		if(index < this.useSize) {
    			Element frontElement = linkSelect(this.last);
    			frontElement = this.linkList[0];
    			integer i = 0;
    			for(;frontElement.next!=index;i++) frontElement = this.linkList[i];
    			this.linkList[i].next = this.linkList[index].next;
    			this.linkList[this.last].next = index;
    			
    	}
    		}
    }
 */
}

