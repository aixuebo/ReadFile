package com.maming.common.extendsrelation.lession1;

public class Base {

	public void baseTest(){
		System.out.println("Base");
	}
	
	public void invokeExecutor(){
		new ExecutorTest(this);//this到底是父类还是子类
	}

}
