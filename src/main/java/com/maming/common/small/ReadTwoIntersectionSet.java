package com.maming.common.small;

import java.util.HashSet;
import java.util.Set;

import com.maming.common.util.ReadFile;

public class ReadTwoIntersectionSet{
	
    
    Set<String> set1 = new HashSet<String>();
    Set<String> set2 = new HashSet<String>();
    
	String[] arr = null;
	
	public void read1(String path){
		new ReadFile(path){
		    public void init() {
		      this.setEncoding("UTF-8");
		    };
			public void parse(String line) {
			  set1.add(line);
			}
		}.start();
	}
	
	
	public void read2(String path){
		new ReadFile(path){
          public void init() {
            this.setEncoding("UTF-8");
          };
          
			public void parse(String line) {
				set2.add(line);
            }
		}.start();
	}

	public void end(){

		System.out.println("原始set1和set2大小==="+set1.size()+"=="+set2.size());
		
		Set<String> setIntersection =  new HashSet<String>(set1);//交集
		
		setIntersection.retainAll(set2);//将set1与set2获取交集
		
		//删除交集
		set1.removeAll(setIntersection);
		set2.removeAll(setIntersection);
		
		//获取删除交集后的数据
		System.out.println(set1);
		System.out.println(set2);
		System.out.println("去除公共交集后的set1和set2大小==="+set1.size()+"=="+set2.size());

	}
	
	public static void main(String[] args) {
	    ReadTwoIntersectionSet test = new ReadTwoIntersectionSet();
		test.read1("E:\\linux\\temp.txt");
		test.read2("E:\\linux\\temp2.txt");
		test.end();
	}
}
