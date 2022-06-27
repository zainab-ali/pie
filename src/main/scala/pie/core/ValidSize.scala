package pie.core

import cats.NonEmptyTraverse
import cats.data.NonEmptyList
import cats.implicits.*

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
      val ordering: Ordering[Int] = implicitly
//      if(x.size > y.size) { 1 }
//      else if(x.size < y.size) { -1 }
//      else { 0 }

      ordering.compare(x.size, y.size)
    }
  }

val orderingInt: Ordering[Int] = new Ordering[Int] {
  override def  compare(x: Int, y: Int): Int = {
    if(x > y) 1
    else if(x < y) -1
    else 0
  }

  }

  ord.compare(Three, Three) == 0
  ord.compare(Three, Four) == -1
  ord.compare(Four, Three) == +1

    // val intOrd: Ordering[ValidSize] = implicitly

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

  val test: NonEmptyTraverse[NonEmptyList] = implicitly
  val values: NonEmptyList[ValidSize] = NonEmptyList.of(Four, Five, Six, Three)

  //TODO Why is minimum not available?

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

  val minSize: Int = min[ValidSize](values).size


}
