package pie.core
 
sealed trait ValidSize {
  val size: Int

}

object ValidSize {



//    Returns an integer whose sign communicates how x compares to y.
//      The result sign has the following meaning:
//      negative if x y
//      positive if x > y
//      zero otherwise (if x == y)

  implicit val ord = new Ordering[ValidSize]{
    override def  compare(x: ValidSize, y: ValidSize): Int ={
      if(x.size < y) -1
    }
  }

    val intOrd: Ordering[ValidSize] = implicitly

    val minSize: Int = ???
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

    val values: Set[ValidSize] = Set(Three, Four, Five, Six)
}
