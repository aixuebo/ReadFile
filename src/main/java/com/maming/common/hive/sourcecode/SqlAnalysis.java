package com.maming.common.hive.sourcecode;

import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.ParseDriver;
import org.apache.hadoop.hive.ql.parse.ParseException;

/**
 * sql解析
 * 详细sql分析结果参见document---hive下源码分析内容
 */
public class SqlAnalysis {

	public void test1(){
		String command = "select count(distinct (aa)),cc from biao";
		//command = "select count(distinct (aa)),cc ,ROW_NUMBER() OVER (PARTITION BY userid order by find_in_set(iseffective,'1') desc,lastupdatetime asc) as rn from biao";
		command = "select b1.id,b1.name from biao1 b1 left join biao2 b2 on b1.id = b2.id and b.name = b2.name and b.age=20 full join (select * from biao3) b3 on b1.id = b3.id and b1.name = b3.name";
		command = "insert overwrite table databases.temp_user  select fu.user_mobile ,rp.userid ,  str_to_map(concat_ws(',',collect_set(concat( starttime,\"#\", endtime)) ),',','#') wfre from databases.preference_hour rp left join databases.user_info fu on rp.userid = fu.userid and length(user_mobile) = 11  where  rp.log_type='invest' group by user_mobile,rp.userid";
		try {
			ASTNode node = new ParseDriver().parse(command);
			System.out.println(node.dump());
			System.out.println(node);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**

	 */
    public static void main(String[] args) {
    	SqlAnalysis test = new SqlAnalysis();
    	test.test1();
	}
}
