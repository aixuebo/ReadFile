package com.maming.common.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.actor.Props;

public class Test1 {

	public void test1(){
		 ActorSystem system = ActorSystem.create("Hello");
		 ActorRef a = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
		 
		 ActorRef b = system.actorOf(Props.create(ExtendedActorSystem.class), "ExtendedActorSystem");
	}
	
	  public static void main(String[] args) {
		   
		  Test1 test = new Test1();
		  test.test1();
	  }
}
