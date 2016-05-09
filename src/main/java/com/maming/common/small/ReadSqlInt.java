package com.maming.common.small;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.maming.common.util.LogWriter;
import com.maming.common.util.ReadFile;
import com.maming.common.util.StringUtil;


public class ReadSqlInt extends ReadFile{

	public static Logger log = LogWriter.getCommonLog();
	
	public ReadSqlInt(String path){
		super(path,"UTF-8");
	}
	
	@Override
	public void init() {
		this.isTrim = false;
		this.setEncoding("UTF-8");
	}
	
    Set<Integer> set = new HashSet<Integer>();
    
    String[] arr = null;
	@Override
	public void parse(String line) {
		set.add(Integer.parseInt(line));
	}
	
    @Override
    public void parseEnd() {
    	System.out.println(StringUtil.setIntegerToString(set));
    }
    
	public static void main(String[] args) {
		String path = "E:\\linux\\temp.txt";
		ReadSqlInt test = new ReadSqlInt(path);
		test.start();
	}
}

