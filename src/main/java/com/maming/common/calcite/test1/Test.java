package com.maming.common.calcite.test1;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试Interfact1可以被引用到一个对象中,用于未来使用
 * @author maming
 *
 */
public class Test {

	private Map<Object,Object> map = new HashMap<Object,Object>();
	
	public void add(String key,Interfact1 value) {
		
		map.put(key, new Interfact2() {

			@Override
			public String test2() {
				value.test1();
				System.out.println("Interfact2 bbbb");
				return "bbb";
			}
			
		});
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		test.add("aaa", new Interfact1() {

			@Override
			public String test1() {
				System.out.println("Interfact1 bbbb");
				return null;
			}
			
		});
		
		Interfact2 test2 = (Interfact2)test.map.get("aaa");
		test2.test2();
	}
}
