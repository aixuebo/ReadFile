package com.maming.common.thread.Executors;

import java.util.concurrent.Executor;

/**
 * Executor是线程池的金身
 * 也可以当作一个线程来用
 * 
 * 
 * 注意:
 * 1.submit和executor方法的区别
 * 线程池的submit方法是有返回值的,即实现的是future模式
 * 而executor是没有返回值的,就是执行的Runnable的run方法
 * 
 * 2.线程池解决了 性能的问题
 * 
 */
public class ExecutorsTest implements Executor{

	@Override
	public void execute(Runnable command) {
		command.run();
	}

	public void test1(){
		this.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("线程池金身测试");
				
			}
		});
	}
	
	public static void main(String[] args) {
		ExecutorsTest test = new ExecutorsTest();
		test.test1();
	}



}
