package com.maming.common.extendsrelation.lession1.fanshe;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

public class ReflectiveTest {

	class FatherClass<T,E> {

		public void aa() {
			bb(this);
		}
		
		public void bb(FatherClass a) {
			System.out.println(a.getClass());
		}
	}

	class SonClass<N extends Number, 
    	E extends RuntimeException, 
    	C extends Character> extends FatherClass<String,String> {

	}
	
	interface IPerson<T> {

	}
	interface IWalk<T> {

	}
	
	interface IStudent extends IPerson<String>,IWalk<Object>,Cloneable{

	}

	public static void main(String[] args) {

		FatherClass<String,String> father = new ReflectiveTest().new FatherClass<String,String>();
		SonClass son = new ReflectiveTest().new SonClass();
		
		
		System.out.println(father.getClass().getSuperclass());//class java.lang.Object
		System.out.println(father.getClass().getSuperclass().getSuperclass());//null
		System.out.println(son.getClass().getSuperclass());//class com.maming.common.extendsrelation.lession1.fanshe.ReflectiveTest$FatherClass
		
		//
		System.out.println(father.getClass().isInstance(son));//true
		System.out.println(son.getClass().isInstance(father));//false
		
		TypeVariable[] typeArr = father.getClass().getTypeParameters();
		for(TypeVariable a:typeArr) {
			System.out.println(a.getName()+"=="+a.getTypeName());
			Type[] types = a.getBounds();
			System.out.println(Arrays.toString(types));
			for(Type t:types) {
				System.out.println(t.getTypeName());
			}
		}
		
		System.out.println("----");
		typeArr = son.getClass().getTypeParameters();
		for(TypeVariable a:typeArr) {
			System.out.println(a.getName()+"=="+a.getTypeName());
			Type[] types = a.getBounds();
			System.out.println(Arrays.toString(types));
			for(Type t:types) {
				System.out.println(t.getTypeName());
			}
		}
		
		System.out.println("----");
		ParameterizedType t = (ParameterizedType) son.getClass().getGenericSuperclass();
		System.out.println(t.getTypeName());
		System.out.println(t.getClass());
		System.out.println(t.getRawType());
		System.out.println(t.getOwnerType());
		System.out.println(Arrays.toString(t.getActualTypeArguments()));
		System.out.println(t.getActualTypeArguments()[0].getTypeName());
		
		System.out.println("----");

	    System.out.println("start IStudent");
		explainType(IStudent.class.getGenericInterfaces());
		System.out.println("---");
		explainClass(IStudent.class.getInterfaces());
		System.out.println("end");
		
	    System.out.println("start Object");
		explainType(Object.class.getGenericInterfaces());
		System.out.println("---");
		explainClass(Object.class.getInterfaces());
		System.out.println("end");
		
		
	}
	
	public static void explainClass(Class<?>[] interfaces) {
		for(Class t1:interfaces) {
			System.out.println(t1.toString());
		}
	}
	
	public static void explainType(Type[] genericInterfaces) {
		for(Type t1:genericInterfaces) {
			System.out.println(t1.toString());
		}
	}
}
