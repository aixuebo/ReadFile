package com.maming.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Statement;

public class BaseDao {

    public static final int batchSize = 500;

    public List<Map<String,Object>> query(String sql){
        List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            connection = MysqlConnectFactory.getConnection();
            st = connection.prepareStatement(sql.toString());
            rs = st.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Map<String,Object> record = new HashMap<String,Object>();
                for(int i=1; i<=columnCount; i++){
                    record.put(rs.getMetaData().getColumnName(i),rs.getObject(i));
                    System.out.println(rs.getMetaData().getColumnName(i) + "=="+ rs.getObject(i));
                }
                records.add(record);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            MysqlConnectFactory.close(connection, st, rs);
        }
        return records;
    }

    public int insertOrUpdate(String sql){
        Connection connection = null;
        Statement st = null;
        try{
            connection = MysqlConnectFactory.getConnection();
            st = connection.createStatement();
            int record = st.executeUpdate(sql);
            return record;
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            MysqlConnectFactory.close(connection, st ,null);
        }
        return 0;
    }

    public void insertOrUpdateBatch(String sql,List<List<Object>> records){
        Connection connection = null;
        PreparedStatement st = null;
        int count = 0;
        try{
            connection = MysqlConnectFactory.getConnection();
            st = connection.prepareStatement(sql.toString());
            for(int i=0; i < records.size() ; i++){
                if(count >= batchSize){
                    st.executeBatch();
                    count = 0;
                }
                count ++;
                List<Object> record = records.get(i);
                for(int j=0 ;j<record.size(); j++){
                    st.setObject(j+1,record.get(j));
                }
                st.addBatch();
            }
            if(count > 0){
                st.executeBatch();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            MysqlConnectFactory.close(connection, st ,null);
        }
    }
}
