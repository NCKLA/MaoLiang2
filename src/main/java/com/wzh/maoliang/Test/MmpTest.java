package com.wzh.maoliang.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

public class MmpTest {
	/* 我试一哈这个是next一个少一个还是怎么的 */
	public void shishi() {
		Map<Integer, String> outSiderMap = new HashMap<Integer, String>();
		outSiderMap.put(1, "a");
		outSiderMap.put(2, "b");
		outSiderMap.put(3, "c");
		outSiderMap.put(4, "qc");
		outSiderMap.put(5, "w");
		outSiderMap.put(6, "q");
		outSiderMap.put(7, "e");
		outSiderMap.put(8, "qwe");
		outSiderMap.put(9, "ewq");
		outSiderMap.put(10, "www");

		
		Iterator<Integer> iterator =outSiderMap.keySet().iterator();
		
		while(iterator.hasNext()) {
			System.out.println("第一次element： "+iterator.next());
		}
		System.out.println("开始第二次element： "+iterator.next());
		System.out.println("中场休息");
		while(iterator.hasNext()) {
			System.out.println("开始第二次element： "+iterator.next());
		}
		/* next一次就会移除这个，存取不太看得出来是不是随机 很迷 */
		System.out.println("iterator： "+iterator.toString());
	}
	
	public void shishi2() {
		Random rand = new Random();
		/* 从数组题库里suffer */
		
		/* 完 全 随 机 随机不带要抽的题那就是你脸黑 */
		System.out.println("suiji:"+rand.nextInt(30));
		System.out.println("suiji:"+rand.nextInt(30));
		System.out.println("suiji:"+rand.nextInt(30));
		System.out.println("suiji:"+rand.nextInt(30));
		System.out.println("suiji:"+rand.nextInt(30));
	}
	public void shishi3(){
		Map<Integer, String> outSiderMap = new HashMap<Integer, String>();
		outSiderMap.put(6, "q");
		outSiderMap.put(9, "ewq");
		outSiderMap.put(1, "a");
		outSiderMap.put(7, "e");
		outSiderMap.put(4, "qc");
		outSiderMap.put(2, "b");
		outSiderMap.put(10, "www");
		outSiderMap.put(3, "c");
		outSiderMap.put(8, "qwe");
		outSiderMap.put(5, "w");


		List<Integer> keyList = new ArrayList<Integer>(outSiderMap.keySet());
		keyList.sort(Comparator.comparingInt(Integer::intValue));
		System.out.println(keyList.toString());
		
	}
	public static void main(String[] args) {

//		Map<String,List<String>> map=new HashMap<String,List<String>>();
//		//Map 对象存入 用户名,密码,电话号码
//		
//		map.put("username", new ArrayList<String>());
//		map.get("username").add("zhangsan");
//		map.get("username").add("luoxiang");
//		
//		map.put("pwd", new ArrayList<String>());
//		map.get("pwd").add("111111");
//		map.get("pwd").add("222222");
//		
//		map.put("telephone", new ArrayList<String>());
//		map.get("telephone").add("152232323");
//
//
//		
//		System.out.println(map);
//		//Map 转成  JSONObject 字符串
//		JSONObject jsonObj=new JSONObject(map);
//		System.out.println(jsonObj.toString());
		
		String ss = "selectA22";
		System.out.println("====="+ss.indexOf("selectA"));

	}
	public static int longestPalindrome(String s) {
        int count=0;
        int single=0;
        int p=0;
        for(int i=0;i<s.length();i++){
            if((i+1)>=s.length()||!(s.charAt(i)==s.charAt(p)))
            	System.out.println("s.charAt(i)="+s.charAt(i)+"  s.charAt(p)"+s.charAt(p));
                count += ((i-p)/2)*2;
                System.out.println("进入循环 count="+count+"   计算"+(i-p)/2+"  i="+i+"  p="+p);
                p = i;
                if(single==0 &&((i-1)<0||!(s.charAt(i)==s.charAt(i-1)))) {
                    System.out.println("进入single");
                    single=1;
                }
            }
        return count+single;
        }
	 public boolean isRectangleOverlap(int[] rec1, int[] rec2) {
	        // >rec1[0] <rec1[2]
	        // >rec1[1] <rec1[3]
	        // boolean tempt=false;


	        for(int i=0;i<2;i++){
	            if((rec2[i]>rec1[i] && rec2[i]<rec1[i+2] )|| (rec2[i+2]>rec1[i] && rec2[i+2]<rec1[i+2])) {
	                if((rec1[i+1]>=rec2[i+1] && rec1[i+1]<=rec2[(i+3)%4] )|| (rec1[(i+3)%4]>=rec2[i+1] && rec1[(i+3)%4]<=rec2[(i+3)%4])) return true;
	            }
	        }
	        for(int i=0;i<2;i++){
	            if((rec1[i]>rec2[i] && rec1[i]<rec2[i+2] )|| (rec1[i+2]>rec2[i] && rec1[i+2]<rec2[i+2])) {
	                if((rec2[i+1]>=rec1[i+1] && rec2[i+1]<=rec1[(i+3)%4] )|| (rec2[(i+3)%4]>=rec1[i+1] && rec2[(i+3)%4]<=rec1[(i+3)%4])) return true;
	            } 
	        }
	        
	    return false;
	    }

}
