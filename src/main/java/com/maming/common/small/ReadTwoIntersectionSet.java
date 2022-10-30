package com.maming.common.small;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.maming.common.util.ReadFile;

public class ReadTwoIntersectionSet{
	
    
    Set<String> set1 = new HashSet<String>();
    Set<String> set2 = new HashSet<String>();
    
    Map<Long,Integer> map1 = new TreeMap<Long,Integer>();
    Map<Integer,Integer> map2 = new HashMap<Integer,Integer>();
	String[] arr = null;
	int count = 0 ;
	int count1 = 0 ;
	
	public static final SimpleDateFormat SDF_ALL = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	public void read1(String path){
		new ReadFile(path){
		    public void init() {
		      this.setEncoding("UTF-8");
		    };
			public void parse(String line) {
				if(!line.contains("2020-04-25")) {
					return ;
				}
				arr = line.split(",");				
				long current = 0;
				String time = arr[2].replaceAll("\"", "").replaceAll("\"", "");
				int hour = Integer.parseInt(time.substring(10,13).trim());
				try {
					current = SDF_ALL.parse(time).getTime();//毫秒  ---/1000转换成秒
					//System.out.println(SDF_ALL.format(new Date(current))+"=="+time+"=="+current);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
				String table = arr[1].replaceAll("\"", "").replaceAll("\"", "");

				int v1 = Integer.parseInt(arr[3]);
				if(table.startsWith("")) {//我们用的资源
						//System.out.println(time_convert+"=="+time+"=="+table+"==="+v1+"=="+v2);
					
					Integer vv1 = map1.get(current);
					if(vv1 == null) {
						vv1 = 0;
					}
					map1.put(current, vv1 + v1);
					if(hour >= 19 && v1>1000) {
					//	System.out.println(line);
						set1.add(table+"=="+hour);
					}
					count++;
				} else {
					count1++;
				}
				//set1.add();
			}
		}.start();
	}
	
	//时间 任务 用cu数量 未用cu数量
	//聚合成小时
	
	public void read2(String path){
		new ReadFile(path){
          public void init() {
            this.setEncoding("UTF-8");
          };
          
			public void parse(String line) {
				
				arr = line.split("\t");			
			
				long current = 0;
				String time = arr[0];
				try {
					current = SDF_ALL.parse(time).getTime();//毫秒  ---/1000转换成秒
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
				int v1 = Integer.parseInt(arr[4]);
				Integer vv1 = map1.get(current);
				if(vv1 == null) {
					vv1 = 0;
				}
				map1.put(current, vv1 + v1);
				count++;
				
            }
		}.start();
	}

	public void readStr(String str) {
		String[] arr = str.split(",");
		for(String a:arr) {
			set2.add(a.trim());
		}
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
		System.out.println(count+"=="+count1);
		
		for(String s:set1) {
			//System.out.println(s);
		}
		for(Entry<Long,Integer> entry:map1.entrySet()) {
			
			System.out.println(SDF_ALL.format(new Date(entry.getKey()))+","+entry.getKey()+","+entry.getValue());
		}
		

	}
	
	public static void main(String[] args) {
	    ReadTwoIntersectionSet test = new ReadTwoIntersectionSet();
		//test.readStr(str);
		test.end();
	}
}
