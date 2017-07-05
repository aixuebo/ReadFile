package com.maming.common.hive.sourcecode;

import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.ParseDriver;
import org.apache.hadoop.hive.ql.parse.ParseException;

/**
 * sql解析
 */
public class SqlAnalysis {

	public void test1(){
		String command = "select count(distinct (aa)),cc from biao";
		//command = "select count(distinct (aa)),cc ,ROW_NUMBER() OVER (PARTITION BY userid order by find_in_set(iseffective,'1') desc,lastupdatetime asc) as rn from biao";
		command = "select * from biao1 b1 left join biao2 b2 on b1.id = b2.id and b.name = b2.name left join biao3 b3 on b1.id = b3.id";
		try {
			ASTNode node = new ParseDriver().parse(command);
			System.out.println(node.dump());
			System.out.println(node);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) {
    	SqlAnalysis test = new SqlAnalysis();
    	test.test1();
	}
}
