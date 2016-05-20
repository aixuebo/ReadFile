package com.maming.common.small;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HiveConverter {

	/**
	 * 由于hive的输出默认是使用1这个ascii进行编码的,因此需要解码
	 * 返回值是使用\t分割
	 */
	public static String convert(String line){
		try {
			List<String> columnList = new ArrayList<String>();
			List<Byte> list = new ArrayList<Byte>();
			for(byte b:line.getBytes("UTF-8")){//遍历原始信息的字节数组
				if(b==1){//如果遇见hive的默认分隔符,则将list中已经缓存的字节信息打印出去
					columnList.add(convertColumn(list));
					continue;
				}
				list.add(b);
			}
			columnList.add(convertColumn(list));
			return convertColumns(columnList);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将一列column所对应的字节转换成String
	 */
	private static String convertColumn(List<Byte> list){
		try{
			if(list.size() == 0){
				return "";
			}
			byte[] temp = new byte[list.size()];
			for(int i=0;i<list.size();i++){
				temp[i] = list.get(i);
			}
			list.clear();//清空list内容
			return new String(temp,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	//将最终的列集合,转换成String
	private static String convertColumns(List<String> list){
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(String column:list){
			if(i > 0){
				sb.append("\t");
			}
			sb.append(column);
			i++;
		}
		return sb.toString();
	}
}
