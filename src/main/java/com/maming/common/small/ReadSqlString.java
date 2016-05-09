package com.maming.common.small;

import java.util.HashSet;
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
    
    String[] arr = null;
	@Override
	public void parse(String line) {
		set.add(line);
	}
	
    @Override
    public void parseEnd() {
    	System.out.println(StringUtil.setStringToString(set));
    }
    
	public static void main(String[] args) {
		String path = "E:\\linux\\temp.txt";
		ReadSqlString test = new ReadSqlString(path);
		test.start();
	}
}

