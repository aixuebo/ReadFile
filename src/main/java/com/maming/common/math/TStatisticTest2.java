package com.maming.common.math;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

/**
 * T检验测试
 */
public class TStatisticTest2 extends TTest{

	public TStatisticTest2(double globel_mu,double sample_mu,double sample_d,double sample_n) {
		this(globel_mu,sample_mu,sample_d,sample_n,0.05d);
	}
	
	public TStatisticTest2(String conf) {
		String[] arr = conf.split(",");
		if(arr.length != 4) throw new RuntimeException("conf参数异常");
		this.globel_mu = Double.parseDouble(arr[0]);
		this.sample_mu = Double.parseDouble(arr[1]);
		this.sample_d = Double.parseDouble(arr[2]);
		this.sample_n = Double.parseDouble(arr[3]);
	}
	
	
	public TStatisticTest2(double globel_mu,double sample_mu,double sample_d,double sample_n,double a) {
		this.globel_mu = globel_mu;
		this.sample_mu = sample_mu;
		this.sample_d = sample_d;
		this.sample_n = sample_n;
		this.a = a;
	}
	
	private double globel_mu = 0d;//全局均值
	private double sample_mu = 0d;//样本均值
	private double sample_d = 0d;//样本方差
	private double sample_n = 0d;//样本数量
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
	
	public void execute(String name,String conf) {
		System.out.println(name + "----");
		TStatisticTest2 test = new TStatisticTest2(conf);
		test.p();
		test.z();
		test.zhixin();
	}
	public static void main(String[] args) {
		//contorl 15.60,16.39,836.07,2564d
		//merge_all 15.60,17.17,1059.23,2154d
		
		String conf = "3.3d,3.42d,0.16d,35d";//网上例子

		
		TStatisticTest2 test = new TStatisticTest2(conf);
		test.execute("control", "15.60,16.39,836.07,2564d");
		test.execute("merge1", "15.60,18.29,1334.93,520d");
		test.execute("merge2", "15.60,15.80,761.45,541d");
		test.execute("merge3", "15.60,18.18,1167.24,537d");
		test.execute("merge4", "15.60,16.48,982.32,556d");
		test.execute("mergeall", "15.60,17.17,1059.23,2154d");
	

        
		//t = (15.6046485  - 17.16945685) / sqrt (1059.230934/(2154-1))
		
	}

}
