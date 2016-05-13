package com.maming.common.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.maming.common.util.LogWriter;
import com.maming.common.util.ReadFile;
import com.maming.common.util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ReadFileContext extends ReadFile{

	public static Logger log = LogWriter.getShare();
	
	public ReadFileContext(String path){
		super(path,"UTF-8");
	}
	
	@Override
	public void init() {
		this.isTrim = false;
		this.setEncoding("UTF-8");
	}
	
	StringBuffer sb = new StringBuffer();

	Map<String,Integer> countMap = new HashMap<String,Integer>();
	private Map<String,String> map = new HashMap<String,String>();
    Set<String> set = new HashSet<String>();
    
	int count1 = 0;
	int count2 = 0;
	int count3 = 0;
    
    String[] arr = null;
    String[] arr1 = null;
    int i=0;
    private List<String> list = new ArrayList<String>();
	@Override
	public void parse(String line) {
		line = HiveConverter.convert(line);
		arr = line.split("\t");
		System.out.println(arr.length);
		//set.add(line);
	}
	
    @Override
    public void parseEnd() {
    /*	for(String s:set){
    		System.out.println(s);
    	}*/
    }
    int s = 0;
	public static void main(String[] args) {
		String path = "E:\\linux\\000000_0.0";
		ReadFileContext test = new ReadFileContext(path);
		test.start();
	}
}

