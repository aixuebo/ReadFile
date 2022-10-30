package com.maming.common.google;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class Test {

	//将集合使用逗号组成字符串
	public void test1() {
		String str = Joiner.on(",").skipNulls().join("Harry", null, "Ron", "Hermione");//如果是null就跳过---Harry,Ron,Hermione
		System.out.println(str);
		str = Joiner.on(",").useForNull("aaaa").join("Harry", null, "Ron", "Hermione");//将null替换成aaaa---Harry,aaaa,Ron,Hermione
		System.out.println(str);
		
		//组装成list
		str = Joiner.on(",").join(Arrays.asList(1, 5, 7));
		System.out.println(str); //1,5,7

	    // MapJoiner 的使用，将 map 转换为字符串
	    Map<String,String> map = ImmutableMap.of("k1", "v1", "k2", "v2");
	    str = Joiner.on(";").withKeyValueSeparator("=").join(map);//k1=v1;k2=v2
	    System.out.println(str);
	    
	    
	    String[] arr = new String[] {"aa","bb"};
	    List<String> list = Lists.asList("cc", arr);
	    System.out.println(list);
	   
	}
	

	
	public static void main(String[] args) {
		Test test = new Test();
		test.test1();
	}

}
