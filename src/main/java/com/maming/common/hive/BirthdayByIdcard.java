package com.maming.common.hive;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 实现string的birthday方法
 * 1返回出生年月日,-2表示无
 * create temporary function birthday as 'com.maming.common.hive.BirthdayByIdcard';
 */
@Description(name = "birthday",
value = "_FUNC_(str) - return -2 or 19850202 ",
extended = "Example:\n"
+ "  > SELECT birthday('idcard') FROM src LIMIT 1;\n" + "  return 19850202")
public class BirthdayByIdcard extends UDF{
	
	Text result = new Text();

	public BirthdayByIdcard() {
	}

	public Text evaluate(Text s) {
	  if (s == null) {
	    result.set("-2");
	    return result;
	  }
	  result.set(String.valueOf(brithder(s.toString())));
	  return result;
	}
	  
	public int brithder(String idcard){
		int brithder = -2;//表示无
		try{
			if(idcard.length() == 18){
				return Integer.parseInt(idcard.substring(6,14));
			} else if(idcard.length() == 15){
				int year = Integer.parseInt(idcard.substring(6,8));
				if(year<30){//2030年之前的,设置为20年
					return Integer.parseInt("20"+idcard.substring(6,12));	
				}
				return Integer.parseInt("19"+idcard.substring(6,12));
			}
		}catch(Exception ex){
			
		}
		return brithder;
	}
}
