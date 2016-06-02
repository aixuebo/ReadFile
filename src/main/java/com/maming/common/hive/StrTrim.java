package com.maming.common.hive;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 实现string的strTrim方法
 * 去除前后空格
 * create temporary function strTrim as 'com.maming.common.hive.StrTrim';
 */
@Description(name = "strTrim",
value = "_FUNC_(str) - true return trim(str) ",
extended = "Example:\n"
+ "  > SELECT strTrim(' abcd ') FROM src LIMIT 1;\n" + "  return abcd")
public class StrTrim extends UDF{
	
	Text result = new Text();
	Text defaultText = new Text("");

	public StrTrim(){
	}
	  
	public Text evaluate(Text s,Text defaultText) {
	   if (s == null || "".equals(s.toString()) || "null".equals(s.toString())) {
	     result.set(defaultText);
	     return result;
	   }
	   result.set(s.toString().trim());
	   return result;
	}
	  
	public Text evaluate(Text s) {
		return evaluate(s,defaultText);
	}
}
