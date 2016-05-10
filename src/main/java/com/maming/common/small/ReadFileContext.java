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
		arr = line.split("\t");
		arr1 = arr[1].split(":");
		log.info(arr[0]+"\t"+arr1[0]+"\t"+arr1[1]);
		System.out.println(arr[0]+"\t"+arr1[0]+"\t"+arr1[1]);
		i++;
		//set.add(line);
	}
	
    @Override
    public void parseEnd() {
    	System.out.println(StringUtil.setStringToString(set));
    	System.out.println(i);
    	System.out.println(count1+"===="+count2+"=="+count3);
    /*	for(String s:set){
    		System.out.println(s);
    	}*/
    }
    int s = 0;
	public static void main(String[] args) {
		String path = "E:\\linux\\temp1.txt";
		ReadFileContext test = new ReadFileContext(path);
		test.start();
	}
}

