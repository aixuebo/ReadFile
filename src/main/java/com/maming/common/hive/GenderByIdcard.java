package com.maming.common.hive;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 实现string的gender方法
 * 1返回男,2返回女,-2表示无
 * create temporary function gender as 'com.maming.common.hive.GenderByIdcard';  
 */
@Description(name = "gender",
value = "_FUNC_(str) - return 1 or 2 ",
extended = "Example:\n"
+ "  > SELECT gender('idcard') FROM src LIMIT 1;\n" + "  return 1")
public class GenderByIdcard extends UDF{
	
	Text result = new Text();

	public GenderByIdcard() {
	}

	public Text evaluate(Text s) {
	  if (s == null) {
	    result.set("-2");
	    return result;
	  }
	  result.set(String.valueOf(gender(s.toString())));
	  return result;
	}
	  
	public int gender(String idcard){
		int gender = -2;//表示无
		try{
			if(idcard.length() == 18){
				return Integer.parseInt(idcard.substring(16,17))%2;
			} else if(idcard.length() == 15){
				return Integer.parseInt(idcard.substring(14,15))%2;
			}
		}catch(Exception ex){
			
		}
		return gender;
	}
}
