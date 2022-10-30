package com.maming.common.javaio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;


public class FileChannelTest {

	String path = "/Users/maming/Downloads/test.xxx.txt";
	/**
	 * index   5+1
	 * bb 2+1
	 * 1个字节占用,表示没有内容,只有最后换行字符表示的结尾
	 */
	//读取全部内容 10个字节--注意每一个行结尾都是有一个回车字符跟着的，因此结果是10个字节
	public void test1() {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(148); 
			java.nio.channels.FileChannel channel = new FileInputStream(path).getChannel();
			int size = channel.read(buffer);
			System.out.println(channel.size()+"=="+size);
			printBuffer(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 扩展position 
	 * 以及 获取当前position
	 * 
	 * 1.重新设置position位置,大于文件size也是合法的,但是不会真的更改文件大小。文件还是以前的size
	 * 2.如果position更改后,继续读从新位置开始读取，但是如果大于size,则读取不到数据，返回-1
	 * 3.参考test3
	 *  在超过文件长度的位置追加内容,可以增加文件长度
	 */
	public void test2() {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(3); 
			java.nio.channels.FileChannel channel = new FileInputStream(path).getChannel();
			
			int size = channel.read(buffer);
			System.out.println(channel.size()+"=="+size+"=="+channel.position());
		
			java.nio.channels.FileChannel channelnew = channel.position(100);//链式调用,其实不是赋予了新的对象
			System.out.println(channel.size()+"=="+size+"=="+channel.position());
			System.out.println(channelnew.size()+"=="+size+"=="+channelnew.position());
			buffer.flip();
			size = channelnew.write(buffer);
			System.out.println("-----");
			System.out.println(channel.size()+"=="+size+"=="+channel.position());
			System.out.println(channelnew.size()+"=="+size+"=="+channelnew.position());
			printBuffer(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void test3() {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(3); 
			buffer.put((byte)'a');
			buffer.put((byte)'b');
			buffer.put((byte)10);
			FileOutputStream file = new FileOutputStream(path);
			java.nio.channels.FileChannel channel = file.getChannel();

			/**
			 * 读写是两个channel
			 *  read channel = FileChannelImpl.open(fd, path, true, false, this);
			 * writer channel = FileChannelImpl.open(fd, path, false, true, append, this);
			 */
			buffer.flip();
			int size = channel.write(buffer);//写入数据到输出流
			channel.force(true);
			System.out.println(channel.size()+"=="+size+"=="+channel.position());//获取现在的位置
		
			java.nio.channels.FileChannel channelnew = channel.position(100);//切换位置
			System.out.println(channel.size()+"=="+size+"=="+channel.position());
			System.out.println(channelnew.size()+"=="+size+"=="+channelnew.position());
			buffer.flip();
			size = channelnew.write(buffer);//继续写入
			channel.force(true);
			System.out.println("-----");
			System.out.println(channel.size()+"=="+size+"=="+channel.position());
			System.out.println(channelnew.size()+"=="+size+"=="+channelnew.position());//可以看到文件增长了
			printBuffer(buffer);
			
			channel.close();
			file.flush();
			file.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void test4() {
		try {
			Selector selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void printBuffer(ByteBuffer buffer) {
		buffer.flip();
		byte[] arr = buffer.array();
		for(byte b:arr) {
			System.out.println((int)b);
		}
	}
	
	public static void main(String[] args) {
		FileChannelTest test = new FileChannelTest();
		test.test3();
		test.test1();
	}

}
