package com.maming.common.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.flink.api.java.tuple.Tuple2;



public class WordCount {
    public static void main(String[] args) throws Exception {
        // 创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 从文件中读取数据
        String inputPath = "/Users/maming/Downloads/hello.txt";
        DataSet<String> inputDataSet = env.readTextFile(inputPath);

        // 对数据集进行处理，按空格分词展开，转换成(word, 1)二元组进行统计
        // 按照第一个位置的word分组
        // 按照第二个位置上的数据求和
        
        // flatMap 是将一行转换成多行操作，需要参数是FlatMapFunction的子类
        DataSet<Tuple2<String, Integer>> resultSet = inputDataSet.flatMap(new MyFlatMapper())
                //按照哪个字段进行分组，spark中是groupByKey，因为持有的是tuple,但flink没有这个方法，
                //需要自己制定key的选择器。或者传入位置—从0开始计算。或者传入字段名称
                .groupBy(0)
                .sum(1);//sum中的参数1,表示第1个位置进行求和
        resultSet.print();
    }

    // 自定义类，实现FlatMapFunction接口
    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String, Integer>> {

	
		private static final long serialVersionUID = 1L;

		@Override
		public void flatMap(String s, Collector<Tuple2<String, Integer>> out) throws Exception {
		    // 按空格分词
            String[] words = s.split(" ");
            // 遍历所有word，包成二元组输出
            for (String str : words) {
                out.collect(new Tuple2<>(str, 1));
            }
			
		}

	
    }

}