package com.maming.common.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * 运行
 * 方法1
 * akka.Main
 * 参数com.maming.common.akka.HelloWorld即可
 * 方法2
 * 运行com.maming.common.akka.Main2类
 */
public class HelloWorld extends UntypedActor{

	   @Override
	   public void preStart() {
	       final ActorRef greeter =
	               getContext().actorOf(Props.create(Greeter.class), "greeter");
	       greeter.tell(Greeter.Msg.GREET, getSelf());
	   }

	    @Override
	   public void onReceive(Object msg) {
	       if (msg == Greeter.Msg.DONE) {
	           getContext().stop(getSelf());
	       } else {
	           unhandled(msg);
	       }
	  }
	    
	 /*   public static void main(String[] args) {
			
	    	try {
	    		Class c = Class.forName("akka.Main");
	    		Constructor[] arr = c.getConstructors();
	    		System.out.println(arr.length);
	    		for(Constructor con:arr){
	    			System.out.println(con.getParameterTypes());
	    		}
	    		Main m = (Main) c.newInstance();
	    		System.out.println(m.getClass());
	    		System.out.println(c.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
}
