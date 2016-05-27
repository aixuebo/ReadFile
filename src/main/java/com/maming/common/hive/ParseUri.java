package com.maming.common.hive;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * UDF to extract specfic parts from URL For example,
 * 将URI中path部分.根据参数key获取对应的value值,默认查找不到返回""
 * create temporary function  parse_uri as 'com.xuebo.udf.ParseUri';
 */
@Description(name = "parse_uri",
    value = "_FUNC_(uri, key) - extracts a key from a URI's query ",
    extended = "Parts: HOST, PATH, QUERY, REF, PROTOCOL, AUTHORITY, FILE, "
    + "USERINFO\nkey specifies which query to extract\n"
    + "Example:\n"
    + "  > SELECT _FUNC_('/domain_click.txt?src=800455069_wt3LjZiBMY6yqP_aHuzrx-xSU9lHOQ9AmMMfZQ_ULRqpYOhzvkmM2lI4J3S9C7nS&v=2&_uid=1440952978498-2', "
    + "'_uid') FROM src LIMIT 1;\n" + "  '1440952978498-2'")

public class ParseUri extends UDF {

  public ParseUri() {
  }

  public String evaluate(String urlStr, String key) {
    if (urlStr == null || key == null) {
      return "";
    }
    return parseUrl(urlStr,key);
  }
  
  public static String parseUrl(String url,String key) {
      //String url = "/domain_click.txt?src=800455069_wt3LjZiBMY6yqP_aHuzrx-xSU9lHOQ9AmMMfZQ_ULRqpYOhzvkmM2lI4J3S9C7nS&v=2&_uid=1440952978498-2";
      if (!url.contains("?")) {
          return parseParameter(url,key);
      }
      int sp = url.indexOf("?");
      url = url.substring(sp + 1);
      return parseParameter(url,key);
  }
  
  private static String parseParameter(String url,String key) {
      String[] arr = url.split("&", 100);
      int index = 0;
      for (String str : arr) {
          index = str.indexOf("=");
          if (index > 0) {
        	  if(key.equals(str.substring(0, index))){
        		  return str.substring(index + 1);
        	  }
          }
      }
      return "";
  }
  
  public static void main(String[] args) {
	  String url = "/domain_click.txt?src=800455069_wt3LjZiBMY6yqP_aHuzrx-xSU9lHOQ9AmMMfZQ_ULRqpYOhzvkmM2lI4J3S9C7nS&v=2&_uid=1440952978498-2";
	  System.out.println(parseUrl(url,"_uid1"));
  }
}
