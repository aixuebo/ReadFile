package com.maming.common.calcite.design.visit.version1;

public class SqlNode1 extends SqlNode {

	public void visit(SqlNodeVisit visit) {
		visit.visit(this);
	}

}
