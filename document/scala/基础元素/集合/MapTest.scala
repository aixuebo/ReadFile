
/**
  * 可变与不可变的Map
  */
object MapTest {

  /**
    * 测试不可变的Map
    */
  def testInitImmutable(): Unit ={
    //默认添加的Map是不可变的,如果向不可变的Map中添加元素,需要定义var
    var map = Map[String,String]("name" -> "jason","age" -> "500","test_100" -> "test_100","test_101" -> "test_101")
    map += ("city" -> "北京") //新增
    println(map)
  }

  /**
    * 测试可变的Map
    */
  def testInitMutable(): Unit ={
    val map = scala.collection.mutable.Map[String,String]()
    map += ("test" -> "能添加吗")  //添加单个元素;
    map += ("test" -> "改变了") //更新;
    map += ("success" -> "添加成功了吗","anthor" -> "另外一个")  //添加多个元素

    println(map) // Map(success -> 添加成功了吗, anthor -> 另外一个, test -> 改变了)

    //删除操作
    map -= ("test") //删除某一个key
    println(map)
    map.get("test").getOrElse("none") //返回的是Option对象
  }

  /**
    * Map的高级操作
    * 1.transform(f: (A, B) => B) 表示执行map中的每一个key-value,作为参数生产一个new_string结果,将新产生的String代替原来的value,即(key,value) -> (key,new_string)
    *   核心源码:case (key, value) => update(key, f(key, value))
    *   注意:该方法返回的依然是map本身,即this
    *
    * 2.filterKeys,与filter不同,filter表示用key-value对作为参数,返回true的数据,而filterKeys仅仅用key作为参数,返回true的数据
    *   核心源码:for ((k, v) <- self.iterator) yield (k, f(v))
    *   注意:该方法返回新的map

    * 3.mapValues,相当于transform,即key不变,value为f(value)函数处理的结果
    * 核心源码:for ((k, v) <- self.iterator) yield (k, f(v))
    * 注意:该方法返回新的map
    */
  def testOperator(): Unit ={
    //transform
    val map = scala.collection.mutable.Map[String,String]()
    map += ("test" -> "能添加吗")
    map += ("test" -> "改变了")
    map += ("success" -> "添加成功了吗","anthor" -> "另外一个")
    map.transform((key,value) => value.concat("aa")) //每一个value都追加aa字符串
    println(map) //Map(success -> 添加成功了吗aa, anthor -> 另外一个aa, test -> 改变了aa)

    //filterKeys
    println(map.filterKeys(key => key.contains("test"))) //Map(test -> 改变了aa)

    //mapValues
    println(map.mapValues(value => value.concat("aa"))) //Map(success -> 添加成功了吗aaaa, anthor -> 另外一个aaaa, test -> 改变了aaaa)

  }
  def main(args: Array[String]): Unit = {
    testOperator()
  }

}
