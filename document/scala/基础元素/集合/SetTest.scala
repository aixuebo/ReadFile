
object SetTest {

  /**
    * 测试不可变的Set
    */
  def testInitImmutable(): Unit ={
    val set = Set(1,2,3)
    println(set.getClass.getName) //scala.collection.immutable.Set
    println(set) //Set(1, 2, 3)
    //删除操作
    println(set.drop(1)) //Set(2,3)
  }

  /**
    * 测试可变的Set
    */
  def testInitMutable(): Unit ={
    val set1 = scala.collection.mutable.Set.empty[Int] //初始化Set()的集合

    val set = scala.collection.mutable.Set(1,2,3)

    set.add(4)
    set.remove(1)
    println(set)
    println(set.contains(4))

  }


  def testMatchCase(): Unit ={
    val set = scala.collection.mutable.Set(1,2,3)

    val result = set match {
      //如果等于空
      case set: Set[_] if set.size == 0 => 0
      //如果其他,此时写法为了描述case在多行代码的时候,要用{}包裹
      case _ => {
        var sum = 0;
        for(i <- set) {
          sum += i
        }
        sum
      }
    }

    println(result)

  }

  //因为是链表结构,因此获取头部数据和非头部数据是非常快的
  def testHeadTail(): Unit ={
    val set = scala.collection.mutable.Set(1,2,3)
    //以下两个函数进行链表的拆解
    println(set.head) //获取头部数据
    println(set.tail) //获取除了头部数据外的其他数据

    println(set.last) //返回最后一个 3
    println(set.init) //返回除了最后一个之外的 Set(1, 2)

    println(scala.collection.mutable.Set())//Set()
    println(scala.collection.mutable.Set.empty[String])//Set()
    println(scala.collection.mutable.Set().isEmpty) //true

  }

  /**
    * 测试Set的操作
    */
  def testOperator(){
    val set1 = scala.collection.mutable.Set(1,2,3)
    val set2 = scala.collection.mutable.Set(1,2,4)

    println(set1 ++ set2) //Set(1, 2, 3, 4) 并集,并且过滤重复
    println(set1.intersect(set2)) // Set(1, 2) 交集
    println(set1.diff(set2)) //Set(3) 差集,set1有,但set2没有的

  }
  def main(args: Array[String]): Unit = {
    testInitImmutable()
    testInitMutable()
    testMatchCase()
    testHeadTail()
    testOperator()
  }
}
