package com.maming.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import net.sf.json.JSONObject;

/**
 * 操作身份证号码
 */
public class IdCard {

	/**
	 * 获取出生日期
	 */
	public int brithder(String idcard){
		int brithder = -2;//表示无
		if(idcard.length() == 18){
			return Integer.parseInt(idcard.substring(6,14));
		} else if(idcard.length() == 15){
			int year = Integer.parseInt(idcard.substring(6,8));
			if(year<30){//2030年之前的,设置为20年
				return Integer.parseInt("20"+idcard.substring(6,12));	
			}
			return Integer.parseInt("19"+idcard.substring(6,12));
		}
		return brithder;
	}
	
	/**
	 * 获取性别
	 */
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
	
	 /** 身份证号码的正则表达式 */
    private static final String ID_NO_REGEXP = "^((\\d{17}|\\d{14})(\\d|x|X))$";

    /** 18位身份证号的最后一位 校验参数 */
    private static final int[] IDNO_IWEIGHT = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
          
    //计算校验后,最后一位应该是什么值
    private static final String[] IDNO_CCHECK = new String[] { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" }; 
            

    /**
     * 校验身份证号码。 确定10位的身份证，最后一个位置的校验码是否匹配
     * true表示身份证号码验证码通过 false表示不通过
     */
    public static boolean isIdentityNo(String identityNo) {

    	if(StringUtil.trim(identityNo).equals("")) return false;
    	
        if (!match(identityNo, ID_NO_REGEXP)) {
            return false;
        }

        int length = identityNo.length();
        if (length != 15 && length != 18) {
            return false;
        }

        if (length == 18) {
            int total = 0;
            for (int i = 0; i < 17; i++) {
                total += Integer.valueOf(identityNo.substring(i, i + 1)) * IDNO_IWEIGHT[i];
            }
            int mo = total % 11;
            String lastOne = IDNO_CCHECK[mo];
            return identityNo.substring(17).equalsIgnoreCase(lastOne);
        } else {//暂不支持15位校验码校验
           return false ;
        }
    }

    private static boolean match(String str, String regexp) {
        return Pattern.matches(regexp, str);
    }
    
    /**
     * 获取全部前6位身份证号码
     */
    public static List<String> getTop6() {
    	List<String> list = new ArrayList<String>();
    	
    	//读取前6位映射信息
    	String path = "com/maming/common/util/idcardMapping.txt";
    	String jsonStr = StringUtil.StreamToString(path);
    	JSONObject json = JsonUtil.strToJson(jsonStr);
    	Iterator it = json.keys();
    	while(it.hasNext()) {
    		list.add(it.next().toString());
    	}
    	//System.out.println(list.size());
    	return list;
    }
    
    //校验给定身份证后面位置,猜测身份证号码
    public void test1() {
    	
    	Set<String> set = new HashSet<String>();
    	Set<String> setTemp = new HashSet<String>();
    	
    	String guding = "";
    	//身份证由top6 + 出生日期 + 后4位
    	
    	List<String> top6List = IdCard.getTop6();
 
    	String[] dateArr = new String[] {"1985","1986","1987","1988","1989",
    								 "1990","1991","1992","1993","1994",
    								"1995","1996","1997","1998","1999"};
    	//String[] moArr = new String[] {"01","02","03","04","05", "06","07","08","09","10","11","12"};
    	String[] moArr = new String[] {"01","11"};
					
    	for(String top6:top6List) {
    		for(String date:dateArr) {
    			for(String mo:moArr) {
    				String result = "500234"+date+mo+guding;
    				setTemp.add(result);
    				if(isIdentityNo(result) && set.add(result)) {//输出校验成功的号码
    					System.out.println(result);
    				}
    			}
    		}
    	}	

    	
    	System.out.println(setTemp.size()+"==="+set.size());
    }

	public static void main(String[] args) {
		String idcard = "370704198708140417";
		IdCard test = new IdCard();
		System.out.println(test.brithder(idcard));
		System.out.println(test.gender(idcard));
		System.out.println(test.isIdentityNo(idcard));
		//test.getTop6();
		test.test1();
	}
}
