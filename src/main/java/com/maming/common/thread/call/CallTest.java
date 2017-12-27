package com.maming.common.thread.call;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runnable接口的run方法是无返回值的,因此在JDK1.5之后增加了Callable接口,有返回值
 */
public class CallTest implements Callable<String>{

	private String name = "aaa";
	@Override
	public String call() throws Exception {
		return name + "这个是一个测试类";
	}
	
	/**
	 * 1.new Thread(task).start();可以成立,是因为Thread需要runnable接口
	 * 2.而FutureTask<V> implements RunnableFuture<V>,
	 * interface RunnableFuture<V> extends Runnable, Future<V>
	 * 因此FutureTask本身就是Runnable实现类,因此可以这么调用
	 */
	public void test1(){
		FutureTask task = new FutureTask(this);
		try {
			new Thread(task).start();
			System.out.println("----");
			System.out.println(task.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));//为什么要与0进行|操作呢,因为RUNNING的前三位是1,因此|0之后,前三位一定变成了1,后面的29位开始为0,后面追加加1进行计数器计数
    private static final int COUNT_BITS = Integer.SIZE - 3;//一共4个字节表示int,因此是32-3 = 29---二进制是11101
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;//1<<29表示2的29次方,即1+29个0,一共100000000000000000000000000000,此时减去1,表示结果是29个1

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;//-1是32个1,此时-1<<29表示后面追加29个0,即32个1+29个0,然后还要转换成int,即还剩余32位,那就是111+29个0---111状态,具体值-536870912
    private static final int SHUTDOWN   =  0 << COUNT_BITS;//结果就是0----000状态,具体值0
    private static final int STOP       =  1 << COUNT_BITS;//表示1+29个0---001状态,具体值536870912
    private static final int TIDYING    =  2 << COUNT_BITS;//2的二进制表示10,此时追加29个0,表示1+30个0---010状态 表示队列和线程池为空,具体值1073741824
    private static final int TERMINATED =  3 << COUNT_BITS;//3的二进制表示11,此时追加29个0,表示11+29个0---011状态结束状态,具体值1610612736
    //大小关系RUNNING<SHUTDOWN<STOP<TIDYING<TERMINATED

    // Packing and unpacking ctl  总结一下,前三位是用于测试状态,后面29位用于测试线程数量
    private static int runStateOf(int c)     { return c & ~CAPACITY; }//~CAPACITY为11100000000000000000000000000000,即111+29个0,即CAPACITY的0变成1,1变成0,这样进行&操作的时候,仅看前三位是否为1,后面的一定都是0
    private static int workerCountOf(int c)  { return c & CAPACITY; } //CAPACITY表示000+29个1,这样&的时候,前三位的结果一定是0,后面的结果有可能为1
    private static int ctlOf(int rs, int wc) { return rs | wc; } //有1的就是1
    
	
	public void test2(){
		
		System.out.println(Integer.toBinaryString(COUNT_BITS)+"COUNT_BITS:"+COUNT_BITS);
		System.out.println(Integer.toBinaryString(CAPACITY)+"CAPACITY:"+CAPACITY);
		System.out.println(Integer.toBinaryString(RUNNING)+"RUNNING:"+RUNNING);
		System.out.println(Integer.toBinaryString(SHUTDOWN)+"SHUTDOWN:"+SHUTDOWN);
		System.out.println(Integer.toBinaryString(STOP)+"STOP"+STOP);
		System.out.println(Integer.toBinaryString(TIDYING)+"TIDYING"+TIDYING);
		System.out.println(Integer.toBinaryString(TERMINATED)+"TERMINATED"+TERMINATED);
		
		System.out.println(Integer.toBinaryString(~CAPACITY));
		System.out.println(Integer.SIZE);
		System.out.println("11100000000000000000000000000000".length());
		System.out.println(Integer.toBinaryString(-1<<29));
		System.out.println(Integer.toBinaryString(-1));
		System.out.println(Integer.toBinaryString(3));
		
		System.out.println("-----");
		System.out.println(ctl.get());
		System.out.println(Integer.toBinaryString(ctl.get()));
		System.out.println(Integer.toBinaryString(ctl.get()+1));
		System.out.println(runStateOf(ctl.get()+5));

	}
	public static void main(String[] args) {
		CallTest test = new CallTest();
		test.test2();
	}

}
