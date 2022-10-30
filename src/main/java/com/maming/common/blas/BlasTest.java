package com.maming.common.blas;

import java.util.Arrays;

import com.github.fommil.netlib.BLAS;

public class BlasTest {

	private final BLAS blas = BLAS.getInstance();
	
	double[] matrix = new double[]{
	        1, 1, 1, 1, 1,
	        1, 1, 1, 1, 1,
	        1, 1, 1, 1, 1,
	        1, 1, 1, 1, 1,
	        1, 1, 1, 1, 1,
	        1, 1, 1, 1, 1
	    };
	
	double[] dx = {1.1, 2.2, 3.3, 4.4};
	double[] dy = {1.1, 2.2, 3.3, 4.4};
	
	public void test1() {

		System.out.println("---");
		blas.dscal(6, 2.0, matrix, 2, 6);
		
		System.out.println(matrix.length);
		System.out.println(Arrays.toString(matrix));
		
		double answer = blas.ddot(dx.length, dx, 0, dy, 0);
		System.out.println(answer);
		
	}
	
	public static void main(String[] args) {

		BlasTest test = new BlasTest();
		test.test1();
	}

}
