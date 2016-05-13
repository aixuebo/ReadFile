package com.maming.common.redis;

import java.util.Map;
import java.util.Map.Entry;

import com.maming.common.util.ConfigRedisUtils;

public class Test {

	ConfigRedisUtils configRedisUtils = ConfigRedisUtils.getInstance();
	
	public void test1(){
		int i=0;
		Map<String, String> map = configRedisUtils.getAllHashValue("mobileRegion");
		String[] arr;
		for(Entry<String,String> entry:map.entrySet()){
			arr = entry.getValue().split(":");
			System.out.println(entry.getKey()+"\t"+arr[0]+"\t"+arr[1]);
			i++;
		}
		System.out.println("ii:"+i);//287302
	}

	public static void main(String[] args) {
		Test test = new Test();
		test.test1();
	}
}
