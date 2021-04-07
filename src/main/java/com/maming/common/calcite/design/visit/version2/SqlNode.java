package com.maming.common.calcite.design.visit.version2;

public class SqlNode {
	 public void visit(SqlNodeVisit visitor) {
		 if(this instanceof SqlNode1) {
			 visitor.visit((SqlNode1)this);
		 } else if(this instanceof SqlNode2) {
			 visitor.visit((SqlNode2)this);
		 }
	 }
}
