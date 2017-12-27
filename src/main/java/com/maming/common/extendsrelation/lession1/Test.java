package com.maming.common.extendsrelation.lession1;

public class Test {

	public void test1(){
		new Father().invokeExecutor();//主要考察的是ExecutorTest方法里面调用的到底是Father的还是Base的方法
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		test.test1();
	}

}
