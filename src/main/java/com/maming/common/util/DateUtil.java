package com.maming.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public static final DateFormat YMD_SINGLE = new SimpleDateFormat("yyyyMMdd");
	public static final DateFormat YMDH_SINGLE = new SimpleDateFormat(
			"yyyyMMdd:HH");
	public static final SimpleDateFormat SDF_ALL = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss,SSS");
	public static final DateFormat YMD_Middler_SINGLE = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	//多线程方式
    public static final DateTimeFormatter YMD_ = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter YMD = DateTimeFormat.forPattern("yyyyMMdd");
    //当前日期 DateUtil.YMD_.print(System.currentTimeMillis()

    
	//将yyyy-MM-dd 转换成 yyyyMMdd形式
	public static String convert(String delimiterDate) {
	    return YMD.print(YMD_.parseDateTime(delimiterDate));
	}
	
	public static String aggregatorDateBy(String date, int dayNum) {
	    return YMD.print(YMD.parseDateTime(date).plusDays(dayNum));
	}
	
	public static String aggregatorDateMiddle(String date, int dayNum) {
	    return YMD_.print(YMD_.parseDateTime(date).plusDays(dayNum));
	}

	//计算两个时间差多少天
	public static int dayNum(String beginDate,String endDate){//2017-02-01
		return Integer.parseInt(String.valueOf((YMD_.parseDateTime(endDate).getMillis() - YMD_.parseDateTime(beginDate).getMillis()) / (24 * 3600 * 1000)));
	}
    //每个月的开头和结尾
	public void month(){
		
		Date date = new Date();
		String sourceDate = "20150401";
		
		//打印20个月
		for(int i=0;i<20;i++){
			String startDate = DateUtil.dateConvertSingleByDay(sourceDate, i*32);//下个月
			
			try {
				date = DateUtil.YMD_SINGLE.parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//月初
		 	Calendar beginDay = Calendar.getInstance();     
		 	beginDay.setTime(date);  
		 	beginDay.set(Calendar.DAY_OF_MONTH, 1);  
	        
		 	//月末
	        Calendar endDay = Calendar.getInstance();     
	        endDay.setTime(date);  
	        endDay.set(Calendar.DAY_OF_MONTH, endDay.getActualMaximum(Calendar.DAY_OF_MONTH));
	        
	        //打印
	        System.out.println(DateUtil.YMD_Middler_SINGLE.format(beginDay.getTime()) + "\t" +DateUtil.YMD_Middler_SINGLE.format(endDay.getTime()));
		}
			
	}
	
	/**
	 * 返回该日期对应的星期几
	 * 
	 * @param date
	 *            格式为:yyyyMMdd
	 * @return
	 */
	public static int weekday(String date) {
		try {
			int[] weekDays = { 7, 1, 2, 3, 4, 5, 6 };
			Calendar cal = Calendar.getInstance();
			cal.setTime(YMD_SINGLE.parse(date));
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0)
				w = 0;
			return weekDays[w];
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 此方法线程不安全
	 * 
	 * @param formatStr
	 *            : 日期格式 yyyyMMddHHmm
	 * @param type
	 *            Calendar.HOUR_OF_DAY 小时 ;Calendar.MINUTE 分钟
	 * @param minute
	 * @return 根据单位和具体数值进行日期转换
	 */
	public static String dateConvertSingle(String formatStr, int type,
			int minute) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Calendar nowDate = Calendar.getInstance();
		nowDate.add(type, minute);
		return format.format(nowDate.getTime());
	}

	/**
	 * 此方法线程不安全
	 */
	public static String dateConvertSingle(int type, int minute) {
		Calendar nowDate = Calendar.getInstance();
		nowDate.add(type, minute);
		return YMD_SINGLE.format(nowDate.getTime());
	}

	public static String dateConvertSingleByDay(String date, int dayNum) {
		Calendar nowDate = Calendar.getInstance();
		try {
			nowDate.setTime(YMD_SINGLE.parse(date));
			nowDate.add(Calendar.DAY_OF_YEAR, dayNum);
			return YMD_SINGLE.format(nowDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("dateConvertSingleByDay方法,时间转化异常" + date);
		}
	}

	  public static DateTimeZone PST = DateTimeZone.forID("America/Los_Angeles");
	  public static DateTimeFormatter MINUTE_FORMATTER = getDateTimeFormatter("YYYY-MM-dd-HH-mm");

	  public static DateTimeFormatter getDateTimeFormatter(String str) {
	    return getDateTimeFormatter(str, PST);
	  }

	  public static DateTimeFormatter getDateTimeFormatter(String str, DateTimeZone timeZone) {
	    return DateTimeFormat.forPattern(str).withZone(timeZone);
	  }

	  public static long getPartition(long timeGranularityMs, long timestamp) {
	    return (timestamp / timeGranularityMs) * timeGranularityMs;
	  }

	  public static long getPartition(long timeGranularityMs, long timestamp, DateTimeZone outputDateTimeZone) {
	    long adjustedTimeStamp = outputDateTimeZone.convertUTCToLocal(timestamp);
	    long partitionedTime = (adjustedTimeStamp / timeGranularityMs) * timeGranularityMs;
	    return outputDateTimeZone.convertLocalToUTC(partitionedTime, false);
	  }

	  public static DateTime getMidnight() {
	    DateTime time = new DateTime(PST);
	    return new DateTime(time.getYear(), time.getMonthOfYear(), time.getDayOfMonth(), 0, 0, 0, 0, PST);
	  }
	  
	public static void main(String[] args) {
		String date = "20141016";
		String before30Day = DateUtil.dateConvertSingleByDay(date, -29);// 计算前30天的日期
		String before7Day = DateUtil.dateConvertSingleByDay(date, -6);// 计算前7天的日期
		String before3Day = DateUtil.dateConvertSingleByDay(date, -2);// 计算前3天的日期

		System.out.println(before30Day);
		System.out.println(before7Day);
		System.out.println(before3Day);

		System.out.println(DateUtil.weekday("20141203"));
		System.out.println(dayNum("2017-02-04","2017-02-04"));
		
		
		System.out.println("-----");
		long outfilePartitionMillis = 86400000;
		DateTimeZone outputTimeZone = DateTimeZone.forID("Asia/Shanghai");
		DateTimeFormatter outputDirFormatter = getDateTimeFormatter("'hourly'/YYYY/MM/dd/HH", outputTimeZone).withLocale(Locale.US);
		System.out.println("-----"+outputDirFormatter);
	
		long a = getPartition(outfilePartitionMillis, 1622476900000l, outputDirFormatter.getZone());//
		DateTime bucket = new DateTime(Long.valueOf(a));
		System.out.println(bucket.toString(outputDirFormatter)+"-----"+a+"=="+new Date(1622476900000l));
		

	}
}
