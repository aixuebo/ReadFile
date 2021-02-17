
/**
  * ListBuffer 很少用,基本上和ArrayBuffer用处相同
  * 只是Array是用数组的形式实现的,因此查询、插入速度、删除速度都很快，但扩容的会慢。
  * 而ListBuffer是链表形式实现的,因此查询会慢,插入、删除、扩容都很快。
  */
object ListBufferTest {

  //初始化ListBuffer 以及 转换成List
  def testInit(): Unit ={
    val buf = scala.collection.mutable.ListBuffer.empty[Int]
    buf += 1
    buf += 10
    println(buf)

    scala.collection.mutable.ListBuffer[Int]() //此时返回的是Nil

    val buf1 = scala.collection.mutable.ListBuffer[Int](1,2,3)
    println(buf1)
    println(buf1.toList)
  }

  //List在match case上的应用
  def testMatchCase(): Unit ={
    val list = scala.collection.mutable.ListBuffer[Int](1,2,3,4,5).toList
    val result = testMatchCase(list)
    println(result)
  }

  //针对list参数,返回int
  def testMatchCase(list:List[Int]): Int ={
    list match {
      //如果等于空
      case Nil => 0
      //如果只包含一个元素,则返回该元素
      case x :: Nil => x
      //如果包含2个元素以上,返回前面2个元素之和,即 完全等价于case List(x, y) => x + y
      case x :: y :: Nil => x + y + 5
      // 如果超过2个元素以上,则使用排除前面2个元素后的数据求和
      case x:: y:: (others:List[Int]) => {
        others.sum
      }
      //如果其他,此时写法为了描述case在多行代码的时候,要用{}包裹
      case _ => {
        var sum = 0;
        for(i <- list) {
          sum += i
        }
        sum
      }
    }
  }

  //因为是链表结构,因此获取头部数据和非头部数据是非常快的
  def testHeadTail(): Unit ={
    val list = scala.collection.mutable.ListBuffer[Int](1,2,3,4,5).toList
    //以下两个函数进行链表的拆解
    println(list.head) //获取头部数据
    println(list.tail) //获取除了头部数据外的其他数据

    println(list.last) //返回最后一个 5
    println(list.init) //返回除了最后一个之外的 List(1, 2, 3, 4)
  }

  def main(args: Array[String]): Unit = {
    testInit()
    testMatchCase()
    testHeadTail()
  }

}
