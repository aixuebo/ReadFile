
import scala.collection.mutable.ArrayBuffer

/**
  * 可变数组,相当于java的ArrayList
  */
object ArrayBufferTest {

  /**
    * 初始化
    */
  def testInit(): Unit ={

    val aa = scala.collection.mutable.ArrayBuffer.empty[Int] //定义空集合,调用empty,就不需要(),这个就是与下一个定义的区别

    val a = new ArrayBuffer[Int](16)
    a += 1 //在尾部追加元素
    a += 2
    a += (1,2,3,5) //在尾部追加多个元素
    println(a) //ArrayBuffer(1, 2, 1, 2, 3, 5)

    a ++= Array(8, 13, 21) //用++=操作符追加任何集合
    a ++= ArrayBuffer[Int](1,2,3)
    println(a) //ArrayBuffer(1, 2, 1, 2, 3, 5, 8, 13, 21, 1, 2, 3)

    //在数组缓冲的尾端添加或移除元素是一个高效的操作
    a.trimEnd(5) // 移除最后5个元素
    println(a) // ArrayBuffer(1, 2, 1, 2, 3, 5, 8)
    //也可以在任意位置插入或移除元素，但这样的操作并不那么高效。所有在那个位置之后的元素，都必须被平移
    a.insert (2,6) //在下标2位置后面追加6
    a.remove (2,3) //移除index为2和3的两个元素

  }

  /**
    * 调用java的方法,java方法参数ArrayList,返回值List
    * scala想要调用java的方法:
    */
  def testJavaMethod(): Unit ={

    import scala.collection.JavaConversions.bufferAsJavaList
    import scala.collection.mutable.ArrayBuffer

    val command = ArrayBuffer("ls", "-al", "/home/cay")
    val pb = new ProcessBuilder(command) // Scala到Java的转换 --- ProcessBuilder(List<String> command)

    //注意:java返回值是List的,scala不能使用ArrayBuffer——包装起来的对象仅能保证是个Buffer
    import scala.collection.JavaConversions.asScalaBuffer
    import scala.collection.mutable.Buffer
    val cmd: Buffer[String] = pb.command() //java返回值public List<String> command()

  }
  def main(args: Array[String]): Unit = {
    testInit()
  }

}
