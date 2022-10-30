package com.maming.common.math;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

/**
 * T检验测试
 */
public class TStatisticTest extends TTest{

	
	private double getConfidenceIntervalWidth( double significance) {
	    TDistribution tDist = new TDistribution(34);
	    double a = tDist.inverseCumulativeProbability(1.0 - significance / 2);
	    //return a * statistics.getStandardDeviation() / Math.sqrt(35);
	    return 0;
	}
	
	private double globel_mu = 3.3d;//全局均值
	private double sample_mu = 3.42d;//样本均值
	private double sample_d = 0.16d;//样本方差
	private double sample_n = 35d;//样本数量
	private double a = 0.05;//置信度
	
	//p值
	public void p() {
		double d = tTest(sample_mu,globel_mu,sample_d,sample_n);
		 System.out.println("p值为:"+d+",是否支持原假设:"+!(d < a));
	}
	
	//z值
	public void z() {
		 double sample_z = t(sample_mu,globel_mu,sample_d,sample_n); //计算T检验的值 
		 TDistribution tDist = new TDistribution(sample_n-1);
		 double a_z = tDist.inverseCumulativeProbability(1.0 - a / 2);
		 System.out.println("样本均值所在z值为:"+sample_z+","+a+"所在z值为:"+a_z); //样本均值所在z值为:1.7748239349298862,0.05所在z值为:2.0322445093177173
	}
	
	public void zhixin() {
		 TDistribution tDist = new TDistribution(sample_n-1);
		 //2.0322445093177173  给一个p值,可以得到该p值对应的z值,即0.05这个点的概率对应z值
		 double a_z = tDist.inverseCumulativeProbability(1.0 - a / 2); 
		 System.out.println(a+"所在z值为:"+a_z);//0.05所在z值为:2.0322445093177173
		 
		 double zhixin_down = globel_mu - a_z * Math.sqrt(sample_d/sample_n);
		 double zhixin_up = globel_mu + a_z * Math.sqrt(sample_d/sample_n);
		 System.out.println("置信下界:"+zhixin_down+",置信上界:"+zhixin_up+",置信差值:"+(zhixin_up - zhixin_down));
	}
	
	//p值
	public void p1() {
		double d = tTest(sample_mu,globel_mu,sample_d,sample_n);
		 System.out.println("p值为:"+d+",是否支持原假设:"+!(d < a));
	}
	
	//z值
	public void z1() {
		 double e = t(sample_mu,globel_mu,sample_d,sample_n); //计算T检验的值 
		 System.out.println(e); //1.7748239349298862
		 double f = (sample_mu-globel_mu)/ Math.sqrt(sample_d/sample_n);
		 System.out.println(f);
	}
	
	public void zhixin1() {
		 TDistribution tDist = new TDistribution(sample_n-1);
		 double b = tDist.inverseCumulativeProbability(1.0 - a / 2); //2.0322445093177173  给一个p值,可以得到该p值对应的z值
		 System.out.println(b);
		 System.out.println(tDist.inverseCumulativeProbability(a / 2));//-2.0322445093177177
		 
		 System.out.println(b * Math.sqrt(sample_d/sample_n));//0.13740480749588282
		 System.out.println(tDist.inverseCumulativeProbability(a / 2) * Math.sqrt(sample_d/sample_n));//-0.13740480749588285
		 
		 System.out.println(globel_mu + b * Math.sqrt(sample_d/sample_n));//3.4374048074958825
		 System.out.println(globel_mu - b * Math.sqrt(sample_d/sample_n));//3.162595192504117
		 System.out.println(tDist.inverseCumulativeProbability(1-0.08488133018230136/2));//1.774823935357827
	}
	
	public static void main(String[] args) {
		TStatisticTest test = new TStatisticTest();
		test.p();
		test.z();
		test.zhixin();
	}

}
