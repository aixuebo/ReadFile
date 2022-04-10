
import org.apache.spark.util.collection.OpenHashSet

/**
  * 若长度固定则使用Array，若长度可能有变化则使用ArrayBuffer
  * 用()来访问元素
  * Scala数组和java数组可以互操作；用AnayBuffer，使用scalacollection.JavaConversions中的转换函数
  */
object ArrayTest {

  /**
    * Scala数组比java数组多的能力
    * 1.是一种泛型,即可以定义一个Array[T]
    * 2.Scala数组与Scala序列是兼容的,在需要Seq[T]的地方可由Array[T]代替
    * 3.Scala数组支持所有的序列操作,比如arr.map、filter等操作
    */
  def testInit(): Unit ={
    val arrInt : Array[Int] = Array(1, 2, 3) //初始化,类型是推断出来的,已提供初始值就不需要new了
    arrInt.map(_*2) //支持序列操作
    arrInt(2) = 100 //赋值元素内容

    val arrInt2 = new Array[Int] (10) //所有元素初始化为0
    val arrString =new Array [String] (10) //所有元素初始化为null
    println(arrInt2(1)) //0 并且 下标使用()代替[]
    println(arrString(1)) //null


    //高级创建数组,并且赋值
    val numBins = Array.fill[Int](3)(50) //初始化3个元素的int数组,并且赋予默认值为50
    numBins.foreach(println(_))

    val featureValueSets = Array.fill[OpenHashSet[Double]](50)(new OpenHashSet[Double]()) //创建50个size的数组,数组每一个元素是OpenHashSet[Double]对象
  }

  /**
    * scala扩展java功能的原理:隐式转换。
    * scala.collection.mutable.WrappedArray extends AbstractSeq[T] with IndexedSeq[T] 该类是Seq的子类,并且实现了隐式转换。
    *
    */
  def testSource(): Unit ={
    val arrInt : Array[Int] = Array(1, 2, 3) //初始化
    println(arrInt)
    val seq: Seq[Int] = arrInt //隐式转换,转换成WrappedArray,该对象是Seq的子类
    println(seq) //WrappedArray(1, 2, 3)
  }

  /**
    * 创建多维数组
    */
  def testMatrix(): Unit ={
    val matrix = Array.ofDim[Double](3,4) //Array[Array[Double]]
    matrix (1) (2) = 42 //赋值
    matrix.foreach(v => v.foreach(println(_)))
  }

  def testJava(): Unit ={
    val arrInt : Array[Int] = Array(2, 1, 3) //初始化
    java.util.Arrays.sort(arrInt) // java需要参数数组,scala直接使用 sort(int[] a)
    arrInt.foreach(println(_)) // 1 2 3

    import java.util.Arrays
    val data = Arrays.asList(arrInt) //asList参数是数组,返回值是java的List

  }
  def main(args: Array[String]): Unit = {
    testJava()
  }
  
  //定义数组---里面元素是元组  ---以及如何获取数组下标进行循环
  def test1(args: Array[String]): Unit ={

    //定义数组--存储元组
    val fieldArray = Array(("docid", "filed"), ("title", "filed"), ("summary", "filed"), ("date", "filed"))

    println(fieldArray.indices) //Range(0, 1, 2, 3)
    for (i <- fieldArray) {
      println(i._1+"==="+i._2) //docid===filed
    }
    
    for (i <- fieldArray.indices) {
      println(i) //打印下标
      if (fieldArray(i)._2 == "filed") println("是filed")
    }
    
  }
}
