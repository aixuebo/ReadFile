package com.maming.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDao {

    public List<Map<String,Object>> query(String sql){
        List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
        try{
            Connection connection = MysqlConnectFactory.getConnection();
            PreparedStatement st = connection.prepareStatement(sql.toString());
            ResultSet rs = st.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Map<String,Object> record = new HashMap<String,Object>();
                for(int i=1; i<=columnCount; i++){
                    record.put(rs.getMetaData().getColumnName(i),rs.getObject(i));
                    System.out.println(rs.getMetaData().getColumnName(i) + "=="+ rs.getObject(i));
                }
                records.add(record);
            }
            MysqlConnectFactory.close(connection, st, rs);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return records;
    }
}
