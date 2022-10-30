package com.maming.common.extendsrelation.lession1;

public class ExecutorTest {

	Base base;
	
	public ExecutorTest(Base base){
		System.out.println("base====>"+base.getClass());
		this.base = base;
		base.baseTest();
	}
	
	

}
