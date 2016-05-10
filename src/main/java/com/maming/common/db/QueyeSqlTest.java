package com.maming.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class QueyeSqlTest {

	ConnectPool connectPool = ConnectPool.getInstance();
	
	public void query1(){
		try{
			Connection commonConnection = connectPool.getCommonConnection();
			PreparedStatement st = commonConnection.prepareStatement("select * FROM user_info limit 10");
			System.out.println("bbb");
			ResultSet rs = st.executeQuery();
			System.out.println("cc");
			
			System.out.println(rs.next());
			int c = 0;
			 while (rs.next()) {
				 c++;
	                long uid = rs.getLong(1);  
	                String ufname = rs.getString(2);  
	                String ulname = rs.getString(3);  
	                String udate = rs.getString(4);  
	                System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
	         }
			 System.out.println(c);
			 connectPool.close(commonConnection, st, rs);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
	
		QueyeSqlTest test = new QueyeSqlTest();
		test.query1();
	}
}
