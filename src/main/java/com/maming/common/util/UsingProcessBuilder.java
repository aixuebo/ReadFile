package com.maming.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * demo查看test包下代码  com.maming.common.util.UsingProcessBuilderTest
 */
public class UsingProcessBuilder {
	
	// 执行一组命令,返回结果是一个字符串
	public static String executeCommands(String... commands) {
		Process p = null;
		try {
			p = new ProcessBuilder(commands).start();
			return UtilIo.readInputStream(p.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 执行一个脚本
	 * @param env 环境变量
	 * @param directory 工作目录
	 * @param commands 执行的命令
	 * @return 返回正常输出
	 */
	public static String executeScript(Map<String,String> env,String directory,String... commands) {
		Process p = null;
		String result = "";
		String errorResult = "";
		
		try {
			ProcessBuilder pb = new ProcessBuilder(commands);
			Map<String,String> envs = pb.environment(); // 获得进程的环境
			// 设置和去除环境变量
			for(Entry<String, String> e:env.entrySet()){
				if("null".equals(e.getValue())){
					env.remove(e.getKey());
				}else{
					envs.put(e.getKey(), e.getValue());
				}
			}
			
			if(!"".equals(directory)){
				pb.directory(new File(directory));
			}
			
			//开始执行程序
			p = pb.start();
			if (p.waitFor() != 0) {//等待进程执行完毕
				// 如果进程运行结果不为0,表示进程是错误退出的
				// 获得进程实例的错误输出
				errorResult = UtilIo.readInputStream(p.getErrorStream());
			}
			result = UtilIo.readInputStream(p.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("errorResult:"+errorResult);
		return result;
	}

}
