package com.maming.common.hive;

import java.text.SimpleDateFormat;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 实现string转化为对应的日期格式yyyy-MM-dd,因为hive默认只支持yyyy-MM-dd格式,默认参数格式是yyyyMMdd
 * create temporary function  strDateFormat as 'com.xuebo.udf.StrDateFormat';
 */
@Description(name = "strDateFormat",
value = "_FUNC_(date1,sourceDateFormat,targetDateFormat) -",
extended = "Example:\n"
		+ "  > SELECT strDateFormat('2015-01-01','yyyy-MM-dd','yyyyMMdd') FROM src LIMIT 1;\n" + "  return 20150101")
public class StrDateFormat extends UDF{
	  
	  private  Text sourceText = new Text("yyyy-MM-dd");
	  private  Text targetText = new Text("yyyyMMdd");
	  private  SimpleDateFormat sourceFormatter = new SimpleDateFormat(sourceText.toString());
	  private  SimpleDateFormat targetFormatter = new SimpleDateFormat(targetText.toString());
	  
	  Text result = new Text();
	  
	  public StrDateFormat() {
		  
	  }

	  public Text evaluate(Text date) {
		  return evaluate(date,sourceText,targetText);
	  }
	  
	  public Text evaluate(Text date,Text sourceFormat,Text targetFormat) {
	    if (date == null || sourceFormat == null || targetFormat == null) {
	      return null;
	    }
	    
	    if(!sourceText.equals(sourceFormat)){
	    	sourceText.set(sourceFormat);
	    	sourceFormatter = new SimpleDateFormat(sourceText.toString());
	    }
	    
	    if(!targetText.equals(targetFormat)){
	    	targetText.set(targetFormat);
	    	targetFormatter = new SimpleDateFormat(targetText.toString());
	    }
	    
	    try{
	    	result.set(targetFormatter.format(sourceFormatter.parse(date.toString())));
	    	return result;
	    }catch(Exception ex){
	    	return null;
	    }
	  }
}
