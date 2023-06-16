package com.maming.common.cube;

import java.util.ArrayList;
import java.util.List;

public class CubeGenerator {

	private String[] DIMENSIONS = null;//维度内容
    private int DIMENSION = 0;//维度数量
    

    public CubeGenerator(String[] DIMENSIONS) {
    	this.DIMENSIONS = DIMENSIONS;
    	this.DIMENSION = DIMENSIONS.length;
    }
    
    public void generator() {
        for (int i = 0; i < Math.pow(2, DIMENSION); i++) {
            List<String> combination = new ArrayList<>();
            for (int j = 0; j < DIMENSION; j++) {
                if ((i & (1 << j)) != 0) {
                    combination.add(DIMENSIONS[j]);
                }
            }
            
            String result = new StringBuffer().append(combination.toString()).toString()
            			.replaceAll(", ", ",")
            			.replace("[", "(")
            			.replace("]", ")");
            //System.out.println(combination);
            System.out.println(result);

        }
    }
    
    /**
     * 注意事项
     * 因为后三个维度 省市区是有级联关系的，因此可以使用省、市、区，代替各种组两两组合的结果，减少组合次数。 使用的时候可以关联维度表即可 
     * 
     * 步骤:
     * 1.生成组合
     * 2.将组合中，包含两个以及两个以上的"省、市、区"级联维度信息删除掉即可。
     * 
     * 
     * 5个维度，其中3个是可以级联的。因此组合形式 = 32个。来源：
     * 2^2 + 3* (2^2) = 4 + 3 * 4 = 16种。 相当于前两个维度是可以有4种组合，级联相当于单一维度，引入单一维度后，相当于新增4种组合*1个组合。但因为单一维度是级联，有3种可能，因此是4*3
     * 公式如下： 2^可以组合的维度数 + 2^可以组合的维度数 * 级联维度数量
     * 
     * 比如组织结构有8个，那就是 4 + 8 * 4个组合
     */
    public static void main(String[] args) {
    	String[] DIMENSIONS = {"业务类型", "城市级别", "省", "市", "区"}; //需要参与cube计算的维度，理论上会产生2^n排列结果
    	CubeGenerator test = new CubeGenerator(DIMENSIONS);
    	test.generator();
    }

}
