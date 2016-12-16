package com.maming.common.jvm;

import akka.actor.ActorRef;

public class ClassPathTest {

	//获取一个class是哪个jar包下的文件
	public void jarPathByClass(){
		Class<ActorRef> cls = akka.actor.ActorRef.class;
		String path = cls.getResource("/" + cls.getName().replace('.', '/') + ".class").toString();
		//输出 jar:file:/C:/Users/Lenovo/.m2/repository/com/typesafe/akka/akka-actor_2.10/2.3.1/akka-actor_2.10-2.3.1.jar!/akka/actor/ActorRef.class
		System.out.println(path);
		
	    if (path.startsWith("jar:file:")) {//从jar包找到了该class
		    path = path.substring("jar:file:".length(), path.indexOf('!'));
	    }
	    System.out.println(path);
	}
	
	public static void main(String[] args) {
		ClassPathTest test = new ClassPathTest();
		test.jarPathByClass();
	}

}
