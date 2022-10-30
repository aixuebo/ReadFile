package com.maming.common.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class StringUtil {

    public static int toInteger(String value, int defaultValue) {
        try {
            if ("".equals(trim(value))) {
                return defaultValue;
            }
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static int toInteger(String value) {
        return toInteger(value, 0);
    }
    
    public static int toInteger(Integer value) {
        if(value == null) return toInteger("");
        return value;
    }

    public static long toLong(String value, long defaultValue) {
        try {
            if ("".equals(trim(value))) {
                return defaultValue;
            }
            return Long.parseLong(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static long toLong(String value) {
        return toLong(value, 0l);
    }

    public static long toLong(Long value) {
        if(value == null) return toLong("", 0l);
        return value;
    }
    
    public static String trim(String value, String defaultValue) {
        if (value == null || "".equals(value)) return defaultValue;
        else return value.trim();
    }


    public static String trim(String value) {
        return trim(value, "");
    }
    
	public static double toDouble(String value, double defaultValue) {
		try {
			if ("".equals(trim(value))) {
				return defaultValue;
			}
			return Double.parseDouble(value);
		} catch (Exception ex) {
			return defaultValue;
		}
	}
	
	public static double toDouble(String value) {
		return toDouble(value, 0d);
	}

    public static double toDouble(Double value) {
        if(value == null)
            return toDouble("", 0d);
        return value;
    }
    
    public static String cut(String value, int len) {
        if (trim(value).length() > len) {
            return value.substring(0, len);
        }
        return trim(value);
    }

    public static String formatDecimal(String decimalString) {
        DecimalFormat df1 = new DecimalFormat("#,###.00");
        return df1.format(div(decimalString, "100").doubleValue());
    }

    public static String formatDecimal(long decimalLong) {
        DecimalFormat df1 = new DecimalFormat("#,###.00");
        return df1.format(div(String.valueOf(decimalLong), "100").doubleValue());
    }

    public static String formatLong(String decimalLong) {
        return formatLong(Long.parseLong(decimalLong));
    }

    public static String formatLong(int decimalLong) {
        return formatLong(String.valueOf(decimalLong));
    }

    public static String formatLong(long decimalLong) {
        DecimalFormat df1 = new DecimalFormat("#,###");
        return df1.format(decimalLong);
    }

//scala版本BigDecimal.valueOf(0.09).setScale(1,BigDecimal.RoundingMode.DOWN)
    public static BigDecimal div(String value1, String value2) {
        return new BigDecimal(value1).divide(new BigDecimal(value2), 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal div(long value1, int value2) {
        return new BigDecimal(value1).divide(new BigDecimal(value2), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    public static BigDecimal div(double value1, double value2,int n) {
        return new BigDecimal(value1).divide(new BigDecimal(value2), n, BigDecimal.ROUND_HALF_UP);
    }

    public static String setStringToString(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = set.iterator();
        if (it.hasNext()) {
            sb.append('\'').append(it.next()).append('\'');
            while (it.hasNext()) {
                sb.append(",'").append(it.next()).append('\'');
            }
        }
        return sb.toString();
    }

    public static String setIntegerToString(Set<Integer> set) {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> it = set.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(',').append(it.next());
            }
        }
        return sb.toString();
    }

    public static String setLongToString(Set<Long> set) {
        StringBuilder sb = new StringBuilder();
        Iterator<Long> it = set.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(',').append(it.next());
            }
        }
        return sb.toString();
    }

    public static String setIntegerArrToString(int[] arrInt) {
        StringBuilder sb = new StringBuilder(String.valueOf(arrInt[0]));
        for (int i = 1; i < arrInt.length; i++) {
            sb.append(',').append(arrInt[i]);
        }
        return sb.toString();
    }

    public static String parseLineForString(String line, String key, String separator, String defaultStr) {
        int indexBegin = line.indexOf(key);
        if (indexBegin == -1) {//不存在key的时候,返回-1
            return defaultStr;
        }
        if (line.indexOf(separator, indexBegin) == -1) {
            separator = "";
        }
        if (!"".equals(separator)) {
            int indexEnd = line.indexOf(separator, indexBegin);
            String str = line.substring(indexBegin + key.length(), indexEnd).trim();
            return str;
        } else {
            String str = line.substring(indexBegin + key.length()).trim();
            return str;
        }
    }

    public static long parseLineForLong(String line, String key, String separator) {
        return Long.parseLong(parseLineForString(line, key, separator, "-1"));
    }

    public static int parseLineForInteger(String line, String key, String separator) {
        return Integer.parseInt(parseLineForString(line, key, separator, "-1"));
    }

    public static String mapToString(Map<String, String> map) {
        String result = map.toString();
        return result.substring(1, result.length() - 1);
    }

    public static Map<String, String> stringToMap(String mapString) {
        Map<String, String> map = new HashMap<String, String>();
        if (mapString == null || "".equals(mapString)) {
            return map;
        }
        String[] keyValues = mapString.split(",");
        String[] tempArr = null;
        for (String keyValue : keyValues) {
            tempArr = keyValue.split("=");
            if (tempArr.length == 2) {
                map.put(tempArr[0].trim(), tempArr[1].trim());
            } else if (tempArr.length == 1) {
                map.put(tempArr[0].trim(), "");
            }
        }
        return map;
    }

    public static void checkNumber(String value) {
        try {
            Long.parseLong(value);
        } catch (Exception ex) {
            throw new RuntimeException("无法转换成Number类型:" + value);
        }
    }

    public static String formatMessage(String pattern,Object... arr){
    	return MessageFormat.format(pattern, arr);
    }
    
	//保留n位小数
	public static double formatDecimal(double value) {
		return Double.parseDouble(new DecimalFormat("###.00").format(value));
	}
	
	//打印堆栈信息
    public static String printStackTrace(Throwable t){
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }
    
    public static String StreamToString(String path) throws RuntimeException{
    	//String path = "com/maming/common/util/idcardMapping.txt";
    	InputStream inStream = StringUtil.class.getClassLoader().getResourceAsStream(path);
    	try {
    		return IOUtils.toString(inStream, StandardCharsets.UTF_8);
    	} catch (Exception e) {
    		 throw new RuntimeException("StreamToString 转换异常");
    	}
    }
    
    public static void main(String[] args) {
        int[] a = new int[]{5, 6, 7};
        String xx = StringUtil.setIntegerArrToString(a);
        System.out.println(xx);
        System.out.println(formatMessage("/log/statistics/mysql/jlc/{0}/{0}/{1}/part*","aaa","bbb"));
    }
}



