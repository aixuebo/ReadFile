package com.maming.common.calcite;


import org.apache.calcite.sql.*;
import org.apache.calcite.sql.util.*;

import java.util.List;
import java.util.Set;

public class Accept<R> implements SqlVisitor<R>{

   public Set<String> set;//一个sql会重复用多次同一个表,因此要过滤重复

    public Accept(Set<String> set){
       this.set = set;
   }
    /**
     * Visits a literal.
     *
     * @param literal Literal
     * @see SqlLiteral#accept(SqlVisitor)
     */
    public R visit(SqlLiteral literal){
        System.out.println("literal==>"+literal);
        return null;
    }

    /**
     * Visits a call to a {@link SqlOperator}.
     *
     * @param call Call
     * @see SqlCall#accept(SqlVisitor)
     */
    public R visit(SqlCall call){
       // System.out.println("call==>"+call);
        return call.getOperator().acceptCall(this, call);
    }

    /**
     * Visits a list of {@link SqlNode} objects.
     *
     * @param nodeList list of nodes
     * @see SqlNodeList#accept(SqlVisitor)
     */
    public R visit(SqlNodeList nodeList){
       // System.out.println("nodeList==>"+nodeList);

        R result = null;
        for (int i = 0; i < nodeList.size(); i++) {
            SqlNode node = nodeList.get(i);
            result = node.accept(this);
        }
        return result;

    }

    /**
     * Visits an identifier.
     *
     * @param id identifier
     * @see SqlIdentifier#accept(SqlVisitor)
     */
    public R visit(SqlIdentifier id){
        System.out.println("SqlIdentifier==>"+id);
        if(id.toString().startsWith("table.")){ //以谁开头的表名被保留
           set.add(id.toString());
        }
        return null;
    }

    /**
     * Visits a datatype specification.
     *
     * @param type datatype specification
     * @see SqlDataTypeSpec#accept(SqlVisitor)
     */
    public R visit(SqlDataTypeSpec type){
        System.out.println("type==>"+type);
        return null;
    }

    /**
     * Visits a dynamic parameter.
     *
     * @param param Dynamic parameter
     * @see SqlDynamicParam#accept(SqlVisitor)
     */
    public R visit(SqlDynamicParam param){
        System.out.println("param==>"+param);
        return null;
    }

    /**
     * Visits an interval qualifier
     *
     * @param intervalQualifier Interval qualifier
     * @see SqlIntervalQualifier#accept(SqlVisitor)
     */
    public R visit(SqlIntervalQualifier intervalQualifier){
        System.out.println("intervalQualifier==>"+intervalQualifier);
        return null;
    }

}
