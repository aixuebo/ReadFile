package com.maming.common.calcite;


import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import java.util.*;

public class CalciteTest {

	String sql = "select a.* from xx a";
	
	public void test1() {
		// System.out.println(sql);

        SqlParser parser = SqlParser.create(sql);
        //SqlParser parser = SqlParser.create(sql, config.getParserConfig());

        Set<String> set = new HashSet<String>();

        try {
            SqlNode sqlNode = parser.parseStmt();
            //sqlNode.accept(new Accept<String>(set));
            System.out.println(sqlNode.toString());

            System.out.println("-----\n\n\n\n");
            for(String str:set){
                System.out.println(str.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		CalciteTest test = new CalciteTest();
		test.test1();
	}

}
