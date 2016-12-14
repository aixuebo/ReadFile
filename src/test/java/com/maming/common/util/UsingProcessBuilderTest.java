package com.maming.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsingProcessBuilderTest {

	//物理网卡列表
	public static List<String> getPhysicalAddress() {
		List<String> address = new ArrayList<String>();
		
		// 以下是分析输出值,得到物理网卡
		String rtValue = UsingProcessBuilder.executeCommands("ipconfig","/all");
		System.out.println(rtValue);
		int i = rtValue.indexOf("IPv4 地址");
		while (i > 0) {
			rtValue = rtValue.substring(i + "IPv4 地址 . . . . . . . . . . . . : ".length());
			address.add(rtValue.substring(1, 18));
			i = rtValue.indexOf("IPv4 地址");
		}
		return address;
	}

	//执行一个命令,带两个参数
	public static String executeMyCommand1() {
		return UsingProcessBuilder.executeScript(new HashMap<String,String>(),"","myCommand", "myArg1", "myArg2");
	}

	public static void executeMyCommand2() {
		ProcessBuilder pb = null;
		try {
			// 创建一个进程示例
			pb = new ProcessBuilder("cmd.exe");
			// 设置工作目录
			pb.directory(new File("d://myDir"));
			Process p = pb.start();
			// 将要执行的Windows命令写入
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			// '/r/n'是必须写入的
			bw.write("test.bat /r/n");
			bw.write("ping -t www.yahoo.com.cn /r/n");
			bw.flush();
			
			// 将执行结果打印显示
			String result = UtilIo.readInputStream(p.getInputStream());
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		List<String> address = UsingProcessBuilderTest.getPhysicalAddress();
		for (String add : address) {
			System.out.printf("物理网卡地址: %s%n", add);
		}
	}
}
