package com.maming.common.util;

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
	
	public static void main(String[] args) {
		String idcard = "370704198708140417";
		IdCard test = new IdCard();
		System.out.println(test.brithder(idcard));
		System.out.println(test.gender(idcard));
	}
}
