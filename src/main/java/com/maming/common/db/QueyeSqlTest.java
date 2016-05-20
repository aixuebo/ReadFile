package com.maming.common.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class QueyeSqlTest {

	ConnectPool connectPool = ConnectPool.getInstance();
	
	public void query1(){
		try{
			Connection commonConnection = connectPool.getCommonConnection();
			
			StringBuffer sql = new StringBuffer();
			sql.append("select * FROM user_info limit 10");
/*			sql.append("SELECT * FROM(");
			sql.append(" SELECT a.*, b.cur_plus_money,b.cur_regular_money,(b.cur_plus_money + b.cur_regular_money) AS premium ");
			sql.append(" FROM ( SELECT MAX(m1.money_record_id) AS id,m1.userid FROM money_record_additional AS m1 WHERE m1.createDate < '2016-5-11 00:00:00' AND m1.userid IS NOT NULL GROUP BY m1.userId ) AS a");
			sql.append(" LEFT JOIN money_record_additional AS b ON a.id = b.money_record_id HAVING premium > 0) aa ORDER BY premium DESC,userid");
		*/	
			PreparedStatement st = commonConnection.prepareStatement(sql.toString());
			ResultSet rs = st.executeQuery();
			
			int c = 0;
			 while (rs.next()) {
				 c++;
	                Object col1 = rs.getObject(1);  
	                Object col2 = rs.getObject(2);  
	                Object col3 = rs.getObject(3);  
	                Object col4 = rs.getObject(4);
	                Object col5 = rs.getObject(5);  
	                System.out.println(col1 + "\t" + col2 + "\t" + col3 + "\t" + col4 + "\t" + col5);  
	         }
			 System.out.println(c);
			 connectPool.close(commonConnection, st, rs);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void query2(){
		try{
			Connection commonConnection = connectPool.getCommonConnection();
			DatabaseMetaData dbmd = commonConnection.getMetaData();
		    String [] tableTypes = { "TABLE" };
		    ResultSet rs = dbmd.getTables(null, null, null, tableTypes);//获取全部table
		    if (null != rs) {
		      try {
		        while (rs.next()) {
		          System.out.println(rs.getString("TABLE_NAME"));
		        }
		      } finally {
		        rs.close();
		        commonConnection.close();
		      }
		    }
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		QueyeSqlTest test = new QueyeSqlTest();
		test.query2();
	}
}
