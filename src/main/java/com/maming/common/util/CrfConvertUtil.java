package com.maming.common.util;

import java.util.ArrayList;
import java.util.List;

import com.maming.common.vo.BrandCrfTerm;

/**
 * 针对CRF工程字符串处理工具类
 */
public class CrfConvertUtil {

	/**
	 * 训练模型时,从商家名称中标注品牌名
	 * @param poiName 商家名称
	 * @param brandName 品牌名
	 * @return 返回字符+标识的字符串,最后以回车换行结束
	 */
	public List<String> train(String poiName,String brandName) {
		//格式统一处理
		poiName = normal(poiName);
		brandName = normal(brandName);
		
		List<BrandCrfTerm> termList = splitTerm(poiName);
		
		List<String> termFeatureList = new ArrayList<String>();

		int brand_start_index = poiName.indexOf(brandName);
		int brand_end_index = brand_start_index + brandName.length() - 1;
		
		if(brand_start_index < 0) { //品牌名没有在店名里
			return new ArrayList<String>();
		}
		
		for(BrandCrfTerm brandCrfTerm:termList) {
			String term = brandCrfTerm.getTerm();
			int start_index = brandCrfTerm.getStartIndex();
			int end_index = brandCrfTerm.getEndIndex();

			if(brand_start_index > end_index) {
				termFeatureList.add(term+"\tO");
			} else if(brand_start_index == start_index ) {
				termFeatureList.add(term+"\tB");
			} else if(brand_start_index < start_index &&  start_index < brand_end_index ){
				termFeatureList.add(term+"\tM");
			} else if(brand_end_index == end_index ) {
				termFeatureList.add(term+"\tE");
			} else {
				termFeatureList.add(term+"\tO");
			}
		}
		termFeatureList.add("\n");
		return termFeatureList;
	}
	
	/**
	 * 将poi名称转换成term
	 * @param poiName
	 * @return
	 */
	public List<String> predict(String poiName) {
		poiName = normal(poiName);
		List<BrandCrfTerm> termList = splitTerm(poiName);
		
		List<String> termFeatureList = new ArrayList<String>();
		for(BrandCrfTerm brandCrfTerm:termList) {
			termFeatureList.add(brandCrfTerm.getTerm());
		}
		return termFeatureList;
	}
	
	/**
	 * 语法格式化
	 * @param message
	 * @return
	 */
	public static String normal(String message) {
		//TODO trim
		//TODO 格式处理  大小写 ()转换
		message = message.toLowerCase();
		
		return message;
	}
	/**
	 * 将poi名称拆分成CRF需要的格式
	 * 中文--char
	 * 英文--单词
	 * 数字--int
	 * 空格--删除
	 * @param poiName
	 * @return 拆分后的特征集合
	 */
	private List<BrandCrfTerm> splitTerm(String poiName){
		
		List<BrandCrfTerm> list = new ArrayList<BrandCrfTerm>();

		StringBuffer term = new StringBuffer();
		int index = 0;
		int begin = 0;
		char[] ch = poiName.toCharArray();
		while(index < ch.length) {
			//抽取英文
			begin = index;
			while(index < ch.length) {
				if( ((char)ch[index] >= 65 && (char)ch[index] <= 90) || ((char)ch[index] >= 97 && (char)ch[index] <= 122) ) { //英文--65～90为26个大写英文字母，97～122号为26个小写英文字母
					term.append(ch[index]);
					index++;
	        	} else {
	        		break;
	        	}
			}
			if(term.length() > 0) {
				list.add(new BrandCrfTerm(term.toString(),begin,index-1));
				term.setLength(0);
			}
			
			//抽取数字
			begin = index;
			while(index < ch.length) {
				if( (char)ch[index] >= 48 && (char)ch[index] <= 57) { //数字
					term.append(ch[index]);
					index++;
	        	} else {
	        		break;
	        	}
			}

			if(term.length() > 0) {
				list.add(new BrandCrfTerm(term.toString(),begin,index-1));
				term.setLength(0);
			}
        
			//过滤空格
			if(index < ch.length) {
				if((char)ch[index] == 32) { //空格
	        		index++;
	        		continue;
	        	}
			}
        	
			//抽取(非空格、数字、英文)字符
			if(index < ch.length) {
				begin = index;
				list.add(new BrandCrfTerm(String.valueOf(ch[index]),begin,index));
			}
        	index++;
		}
		return list;
	}
	
	public static void main(String[] args) {
		CrfConvertUtil test = new CrfConvertUtil();
		List<String> termFeatureList  = test.train("Dance cream网红生日蛋糕（临汾店）","Dance Cream");
		for(String k:termFeatureList) {
			System.out.println(k);
			//System.out.println("---");
		}
		termFeatureList  = test.train("Dance cream网红生日蛋糕（临汾店）","Dance Cream");
		for(String k:termFeatureList) {
			System.out.println(k);
			//System.out.println("---");
		}
		termFeatureList  = test.predict("Dance cream网红生日蛋糕（临汾店）");
		for(String k:termFeatureList) {
			System.out.println(k);
			//System.out.println("---");
		}
	}
}
