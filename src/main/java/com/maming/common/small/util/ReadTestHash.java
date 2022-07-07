package com.maming.common.small.util;


import java.util.*;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import com.maming.common.util.LogWriter;
import com.maming.common.util.MurmurHash;
import com.maming.common.util.ReadFile;

import scala.util.hashing.MurmurHash3;

/**
 * 测试hash函数碰撞率
 */
public class ReadTestHash extends ReadFile{

	public static Logger log = LogWriter.getCommonLog();
	
	public ReadTestHash(String path){
		super(path,"UTF-8");
	}
	
	@Override
	public void init() {
		this.isTrim = false;
		this.setEncoding("UTF-8");
	}
	
    public static Set<Integer> set = new HashSet<Integer>();
    List<String> list = new ArrayList<String>();
    
    String[] arr = null;
    
    int count = 0;
    
    StringBuffer selectBuffer = new StringBuffer();
     MurmurHash hash = new MurmurHash(0x19264330);
     MurmurHash3 hash3 = new MurmurHash3();
     
	public void parse(String line) {
		byte[] data = line.getBytes();
		for (int i = 0; i < data.length; i++) {
			hash.hash(data[i]);
			
		}
		count++;
		//set.add(hash.hash());
		set.add(hash3.bytesHash(data, 0x19264330));
	}
	
    @Override
    public void parseEnd() {
    	//System.out.println(selectBuffer.toString());
    	System.out.println(set.size()+"=="+count);
    	//4551590==4554022 2432  万分之5的碰撞率
 
    }
    
	public static void main(String[] args) {
		
		String path = "/Users/maming/Downloads/uuid.txt";
		ReadTestHash test = new ReadTestHash(path);
		test.start();
	}
}

