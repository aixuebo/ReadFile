package com.maming.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;


public class QueyeEtlSql extends BaseDao{

	ConnectPool connectPool = ConnectPool.getInstance();
	
	public List<Map<String,Object>> queryAll(){
		String sql = "";
		return super.query(sql);
	}
	/**

*/
	public void query1(){
		try{
			Connection commonConnection = connectPool.getCommonConnection();
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT QUERY FROM metamap_etl WHERE tbl_name LIKE '%topic_channel%' AND valid = 1 ORDER BY  tbl_name");

			PreparedStatement st = commonConnection.prepareStatement(sql.toString());
			ResultSet rs = st.executeQuery();
			
			int c = 0;
			 while (rs.next()) {
				 c++;
	                String col1 = rs.getString(1);  
	                col1 = col1.replace("{{create_time}}", "${create_time}");
	                col1 = col1.replace("{{end_time}}", "${end_time}");
	                col1 = col1.replace("{{period}}", "${period}");
	                col1 = col1.replace("{{create_xxx}}", "${create_xxx}");
	                col1 = col1.replace("{{end_xxx}}", "${end_xxx}");
	                col1 = col1.replace("{{end_time29}}", "${end_time29}");
	                col1 = col1.replace("{{end_time30}}", "${end_time30}");
	                col1 = col1.replace("{{create_xxx1}}", "${create_xxx1}");
	                System.out.println(col1+";");
	                System.out.println();
	                System.out.println();
	         }
			 connectPool.close(commonConnection, st, rs);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		QueyeEtlSql test = new QueyeEtlSql();
		test.query1();
	}
}
