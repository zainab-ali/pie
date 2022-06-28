package pie.core

import cats.data.NonEmptyList

sealed trait ValidSize {
  val size: Int

}

object ValidSize {



//    Returns an integer whose sign communicates how x compares to y.
//      The result sign has the following meaning:
//      negative if x < y
//      positive if x > y
//      zero otherwise (if x == y)
  implicit val ord: Ordering[ValidSize] = new Ordering[ValidSize] {
    override def  compare(x: ValidSize, y: ValidSize): Int = {
      if(x.size > y.size) { 1 }
      else if(x.size < y.size) { -1 }
      else { 0 }
    }
  }


  ord.compare(Three, Three) == 0
  ord.compare(Three, Four) == -1
  ord.compare(Four, Three) == +1

    // val intOrd: Ordering[ValidSize] = implicitly

  val orderingForInt = implicitly[Ordering[Int]]

    val minSize: Int = min[Int](NonEmptyList.fromListUnsafe(validSize.toList))

    case class Mao(name: String)
    implicit val maoOrdering: Ordering[Mao] = myImplicitly
//    implicitly[Ordering[Mao]]

// myImplicitly(myImplicitly(myImplicitly(myImplicitly(myImplicitly(...)))))

    def myImplicitly(implicit e: Ordering[Mao]): Ordering[Mao] = e
    //myImplicitly

    def foo(a:Int)(implicit b: Ordering[Mao]): Unit = {
      ()
    }
    foo(1)(maoOrdering)

    case object Three extends ValidSize {
        override val size: Int = 3
    }
    case object Four extends ValidSize  {
        override val size: Int = 4
    }
    case object Five extends ValidSize {
        override val size: Int = 5
    }
    case object Six extends ValidSize {
        override val size: Int = 6
    }

    val values: Set[ValidSize] = Set(Four, Five, Six, Three)
    val validSize: Set[Int] = Set(Four.size, Five.size, Six.size, Three.size)
//    val minValidSize: ValidSize = min(values.toList)

    def min[A](elements: NonEmptyList[A])(implicit ordering: Ordering[A]): A = {
//      elements.foldLeft(elements.head)((a, b) => {
//        val mile: Int = ordering.compare(a,b)
//        if (mile == 1) b
//        else a
//      })

      elements.reduceLeft((a, b)=>  {
        val mile: Int = ordering.compare(a,b)
        if (mile == 1) b
        else a
      })
    }

}
