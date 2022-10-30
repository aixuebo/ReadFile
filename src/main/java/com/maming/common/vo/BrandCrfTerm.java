package com.maming.common.vo;

/**
 * 品牌CRF分词特征
 * eg:Dance cream网红生日蛋糕（临汾店）  --> 
dance-0-4
cream-6-10
网-11-11
红-12-12
生-13-13
日-14-14
蛋-15-15
糕-16-16
（-17-17
临-18-18
汾-19-19
店-20-20
）-21-21
 */
public class BrandCrfTerm {

	private String term;
	
	private int startIndex;//词在整个名称的开始位置,从0开始计算,包含该位置
	
	private int endIndex;//词在整个名称的结束位置,包含该位置

	public BrandCrfTerm(String term,int startIndex,int endIndex){
		this.term = term;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
}
