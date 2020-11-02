object MatchCaseTest {

  case class Dog(name:String)
  case class Person(first:String,last:String)

  def test(x: Any) : String = {
     x match {
          //常数匹配
          case 0 => "zero"
          case true => "true"
          case "hello" => "you said 'hello'"
          case Nil => "an empty List" //是一个空的list  scala.collection.mutable.ListBuffer.empty[Int]

          // sequence patterns 匹配集合
          case List(0, _, _) => "a three-element list with 0 as the first element" //3个元素,并且第一个元素是0
          case List(1, _*) => "a list beginning with 1, having any number of elements" //很多个元素,并且第一个元素是1
          case Vector(1, _*) => "a vector starting with 1, having any number of elements"

          // tuples
          case (a, b) => s"got $a and $b"
          case (a, b, c) => s"got $a, $b, and $c"

          // constructor patterns
          case Person(first, "Alexander") => s"found an Alexander, first name = $first" //匹配一个对象
          case Dog("Suka") => "found a dog named Suka" //匹配一个对象

          //高级语法,支持匹配类型 && if判断
          case y:Int if(y%5==0) => "5 times Int"

          // typed patterns 类型匹配
          case s: String => s"you gave me this string: $s"
          case i: Int => s"thanks for the int: $i"
          case f: Float => s"thanks for the float: $f"
          case a: Array[Int] => s"an array of int: ${a.mkString(",")}"
          case as: Array[String] => s"an array of strings: ${as.mkString(",")}"
          case d: Dog => s"dog: ${d.name}" //Dog对象
          case list: List[_] => s"thanks for the List: $list"
          case m: Map[_, _] => m.toString

          // the default wildcard pattern
          case _ => "Unknown"
      }
  }

  //针对list参数,返回int
  def testMatchCaseList(list:List[Int]): Int ={
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
        var sum = 0
        for(i <- list) {
          sum += i
        }
        sum
      }
    }
  }

  //针对set
  def testMatchCaseSet(): Unit ={
    val set = scala.collection.mutable.Set(1,2,3)

    val result = set match {
      //如果等于空
      case set: Set[_] if set.size == 0 => 0
      //如果其他,此时写法为了描述case在多行代码的时候,要用{}包裹
      case _ => {
        var sum = 0
        for(i <- set) {
          sum += i
        }
        sum
      }
    }
    println(result)
  }


  def main(args: Array[String]): Unit = {
    println(test("test"))
  }

}