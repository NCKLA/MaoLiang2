package com.wzh.maoliang.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class initRandomIteratorUtils {
	
	public initRandomIteratorUtils(){
	this.size = 30;
	AlterArraySize(this.size);
	}
	
	public initRandomIteratorUtils(int size){
	this.size = size;
	AlterArraySize(this.size);
	}
	
	private int size;
	
	private Integer[] arrayroll;
	private List<Integer> roll;
	
	public Iterator<Integer> ShuffleIteritor() {
		Collections.shuffle(roll);
		return roll.iterator();		
	}
	public void AlterArraySize(int newSize) {
		arrayroll = new Integer[this.size];	
		for(int i=0;i<arrayroll.length;i++) arrayroll[i]=i;	
		roll = Arrays.asList(arrayroll);	
		roll.toArray(arrayroll);	
	}
}
