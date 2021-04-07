package com.maming.common.calcite.design.visit.version1;

public class VisitTest {

	public static void main(String[] args) {
		SqlNodeVisit visit = new SqlNodeVisitImpl();
		SqlNode test = new SqlNode1();
		test.visit(visit);
	}

}
