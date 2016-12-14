package com.maming.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 处理输入流和输出流
 */
public class UtilIo {

	//从输入流中读取数据,到字符串中
	public static String readInputStream1(InputStream in){
		StringBuffer sb = new StringBuffer();
		// 读取命令的输出内容
		byte[] b = new byte[1024];//缓冲区
		int readbytes = -1;
		try {
			while ((readbytes = in.read(b)) != -1) {
				sb.append(new String(b, 0, readbytes,Constants.ENCODE));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	//从输入流中读取数据,到字符串中
	public static String readInputStream(InputStream in){
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, Constants.ENCODE));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}
