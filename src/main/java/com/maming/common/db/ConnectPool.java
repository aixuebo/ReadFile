package com.maming.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.maming.common.util.PropertiesUtils;

public class ConnectPool {

	PropertiesUtils pertiesUtils = PropertiesUtils.getInstance();

	Connection commonConnection = null;
	
	private static ConnectPool INSTANCE = new ConnectPool();
	
	public static ConnectPool getInstance() {
		return INSTANCE;
	}
	
	private ConnectPool(){
		init();
	}
	
	public void init(){
		try {  
            Class.forName(pertiesUtils.getDriverClassName());
            commonConnection = DriverManager.getConnection(pertiesUtils.getCommonUrl(), pertiesUtils.getCommonUser(), pertiesUtils.getCommonPass());
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}

	public void close(ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close(PreparedStatement st,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(st!=null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close(Connection conn,PreparedStatement st,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(st!=null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Connection getCommonConnection() {
		return commonConnection;
	}
}
