
/**
  * 优先队列
  */
object PriorityQueueTest {

  def testInit(): Unit ={
    val pq = collection.mutable.PriorityQueue(1, 2, 5, 3, 7)
    pq += 8
    println(pq)// PriorityQueue(8, 5, 7, 1, 3, 2)
    println(pq.clone.dequeueAll) // Vector(8, 7, 5, 3, 2, 1)


    //基于链表实现的,因此获取第一个,和剩余的,是很快的。
    println(pq.head) //返回第一个 8
    println(pq.tail) //返回除了第一个之外的 PriorityQueue(7, 5, 1, 3, 2)

    println(pq.last) //返回最后一个 2
    println(pq.init) //返回除了最后一个之外的 PriorityQueue(8, 5, 7, 1, 3)

    println(pq.slice(1,3)) // PriorityQueue(7, 5) 获取前面几个元素,取值范围是[1,3)

  }

  //测试正常队列
  def testQueue(): Unit ={
    val pq = collection.mutable.Queue(1, 2, 5, 3, 7)
    pq += 8
    println(pq) //Queue(1, 2, 5, 3, 7, 8),即没有排序
  }

  def main(args: Array[String]): Unit = {
    testInit()
    testQueue()
  }

}
