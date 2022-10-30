package com.maming.common.small;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.maming.common.util.LogWriter;
import com.maming.common.util.ReadFile;
import com.maming.common.util.StringUtil;


public class ReadSqlString extends ReadFile{

	public static Logger log = LogWriter.getCommonLog();
	
	public ReadSqlString(String path){
		super(path,"UTF-8");
	}
	
	
	
	@Override
	public void init() {
		this.isTrim = false;
		this.setEncoding("UTF-8");
	}
	
    Set<String> set = new HashSet<String>();
    List<String> list = new ArrayList<String>();
    
    String[] arr = null;
    
    int count = 0;
    

	@Override
	public void parse(String line) {
		if(line.split("\t")[1].equals("0")) {
			//System.out.println(line);
			//set.add(line.split("\t")[0]);
		}
		set.add(line.split("\t")[0]);
	}
	
	//输出字段
	public void parse1(String line) {
		//System.out.println(line);
		arr = line.split(" ");
		String columnName = arr[0];
		String columnType = arr[1].replace(",", "");
		
		if(columnType.equals("bigint") || columnType.equals("int") || columnType.equals("double")) {
			list.add("coalesce("+columnName+",0) " + columnName +",");
		} else if (columnType.equals("string")) {
			list.add("coalesce("+columnName+",'') " + columnName +",");
		} else {
			list.add("error");
			count++;
		}
		
		//set.add(name.trim());
	}
	
	//拼接成key=value,key=value形式
	StringBuffer sb = new StringBuffer("concat(");
	public void parse2(String line) {
		//System.out.println(line);
		arr = line.split(" ");
		String columnName = arr[0];
		
	}
	
    @Override
    public void parseEnd() {
  
    	
    	//System.out.println(StringUtil.setStringToString(set));
    	for(String str:set) {
    		//System.out.println(str);
    	}
    	//System.out.println(sb.toString());
    	System.out.println(set.size());
    	//146312 - 129949
    	
    }
    
	public static void main(String[] args) {
		String path = "E:\\linux\\temp.txt";
		path = "/Users/maming/Downloads/用于排查问题--txt内容--允许存储中文.txt";
		ReadSqlString test = new ReadSqlString(path);
		test.start();

	}
}

