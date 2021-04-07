学习visit的设计模式
1.父类定义了抽象方法visit(SqlNodeVisit visitor) 要求子类必须实现该方法
2.SqlNodeVisit接口的实现类,需要针对不同的子类有不同的实现方式。
3.当子类确定了,即扔给SqlNodeVisit即可,SqlNodeVisit得到该子类后,就可以进行运算了。
即SqlNodeVisit其实关注的就是子类具体是谁而已。

参见子类SqlNode2
4.main函数,SqlNode test 永远指向父类
	public static void main(String[] args) {
		SqlNodeVisit visit = new SqlNodeVisitImpl();
		SqlNode test = new SqlNode1();
		test.visit(visit);
	}

5.注意:
a.虽然SqlNode1和SqlNode2中visit方法都相同,但不能把他们抽取到父类中,因为visit要求的参数是具体的子类。
b.如果换成visit接口是父类,也可以，但这个时候就需要做分流了。参见verson2包
public class SqlNode {
	 public void visit(SqlNodeVisit visitor) {
		 if(this instanceof SqlNode1) {
			 visitor.visit((SqlNode1)this);
		 } else if(this instanceof SqlNode2) {
			 visitor.visit((SqlNode2)this);
		 }
	 }
}
缺点是父类需要知道所有的子类,这个显然是不太科学的。