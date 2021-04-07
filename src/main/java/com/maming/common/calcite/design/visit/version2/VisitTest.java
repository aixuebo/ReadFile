package com.maming.common.calcite.design.visit.version2;

public class VisitTest {

	public static void main(String[] args) {
		SqlNodeVisit visit = new SqlNodeVisitImpl();
		SqlNode test = new SqlNode2();
		test.visit(visit);
	}

}
