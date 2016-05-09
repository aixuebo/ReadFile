package com.maming.common.small;

import java.util.ArrayList;
import java.util.List;

import com.maming.common.util.ReadFile;

public class ReadTwoIntersectionList{
	
    
    List<String> list1 = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();
    
	String[] arr = null;
	
	public void read1(String path){
		new ReadFile(path){
		    public void init() {
		      this.setEncoding("UTF-8");
		    };
			public void parse(String line) {
				list1.add(line);
			}
		}.start();
	}
	
	
	public void read2(String path){
		new ReadFile(path){
          public void init() {
            this.setEncoding("UTF-8");
          };
          
			public void parse(String line) {
				list2.add(line);
            }
		}.start();
	}

	public void end(){

		System.out.println("原始list1和list2大小==="+list1.size()+"=="+list2.size());
		
		List<String> listIntersection = new ArrayList<String>(list1);//交集
		
		listIntersection.retainAll(list2);//将set1与set2获取交集
		
		//删除交集
		list1.removeAll(listIntersection);
		list2.removeAll(listIntersection);
		
		//获取删除交集后的数据
		System.out.println(list1);
		System.out.println(list2);
		System.out.println("去除公共交集后的list1和list2大小==="+list1.size()+"=="+list2.size());

	}
	
	public static void main(String[] args) {
	    ReadTwoIntersectionList test = new ReadTwoIntersectionList();
		test.read1("E:\\linux\\temp.txt");
		test.read2("E:\\linux\\temp2.txt");
		test.end();
	}
}
