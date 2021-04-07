package com.maming.common.calcite.design.visit.version1;

public class SqlNodeVisitImpl implements SqlNodeVisit {

	@Override
	public void visit(SqlNode1 sqlNode) {
		System.out.println("visit SqlNode1");
	}

	@Override
	public void visit(SqlNode2 sqlNode) {
		System.out.println("visit SqlNode2");
	}

}
