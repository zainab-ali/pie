package pie.core
 
sealed trait ValidSize {
  val size: Int
}

object ValidSize {
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
