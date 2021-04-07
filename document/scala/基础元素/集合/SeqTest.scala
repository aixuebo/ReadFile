
/**
  * Seq是比Array高级一些的序列,他可以存储多个类型的数据
  */
object SeqTest {

  /**
    * Seq是一个序列,与Array完全兼容
    */
  def testInit(): Unit ={
    //从array隐式转换成Seq
    val arrInt : Array[Int] = Array(1, 2, 3)
    val seq: Seq[Int] = arrInt //因此需要Seq的参数,完全可以使用Array代替掉

    //直接填充seq
    val seq2 : Seq[String] = Array.fill(5)("s") //填充5个s的元素数组,转换成seq
    seq2.foreach(println(_))

    //直接填充seq
    val seq3 : Seq[(String,String)] = Array.fill(5)(("s","c")) //填充5个(s,c)的元组元素数组,转换成seq
    seq3.foreach(println(_))

    //简单的定义Seq
    //定义的序列size=3,每一个元素是一个元组,存储元组的可以是多种数据类型
    val a = Seq((7, "US", 18, 1.0),
      (8, "CA", 12, 0.0),
      (9, "NZ", 15, 0.0)
    )
    println(a) //List((7,US,18,1.0), (8,CA,12,0.0), (9,NZ,15,0.0))

  }


  def main(args: Array[String]): Unit = {
    testInit()
  }
}
