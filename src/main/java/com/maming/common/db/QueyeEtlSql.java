package com.maming.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class QueyeEtlSql extends BaseDao{

	ConnectPool connectPool = ConnectPool.getInstance();
	

    public List<Map<String,Object>> queryAll(){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM MARKPOINT_REALTIME where id >= 3237478  limit 10");
        return super.query(sql.toString());
    }

    public void delete(){
        StringBuffer sql = new StringBuffer();
        sql.append("delete FROM JLC_APP_MARKPOINT_REALTIME where id >= 3237478 ");
        super.insertOrUpdate(sql.toString());
    }

    public void insert(){
        String sql = "INSERT INTO MARKPOINT_REALTIME (create_date,create_minute,appversion,packagename,platform,channel,regnum,investamount) " +
                "values(?,?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE regnum = ?,investamount=?";//更改多个属性
        List<List<Object>> records = new ArrayList<List<Object>>();

        List<Object> record = new ArrayList<Object>();
        record.add("2017-11-09");
        record.add("12:55");
        record.add("binlog");
        record.add("binlog");
        record.add("binlog");
        record.add("channel");
        record.add(5);
        record.add(6);
        record.add(51);
        record.add(61);
        records.add(record);

        List<Object> record1 = new ArrayList<Object>();
        record1.add("2017-11-09");
        record1.add("12:56");
        record1.add("binlog");
        record1.add("binlog");
        record1.add("binlog");
        record1.add("channel");
        record1.add(7);
        record1.add(8);
        record1.add(71);
        record1.add(8);
        records.add(record1);
       super.insertOrUpdateBatch(sql,records);
    }
    
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
