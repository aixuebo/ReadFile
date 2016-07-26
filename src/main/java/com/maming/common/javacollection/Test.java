package com.maming.common.javacollection;

import java.nio.channels.ReadableByteChannel;
import java.util.PriorityQueue;
import java.util.concurrent.DelayQueue;

public class Test {

	public void test1(){
		DelayQueue queue = new DelayQueue();
	}
	
	public void test2(){
		PriorityQueue queue = new PriorityQueue();
		ReadableByteChannel chan = null;
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		test.test1();
	}
}
