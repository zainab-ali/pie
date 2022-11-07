package pie

trait Default[A] {
  val default: A
  // TODO: Fill this in
  def getOrDefault(opt :Option[A]): A = opt.getOrElse(default)
}

trait Default2 {
  def default[A]: A
  //def getOrDefault(opt :Option[A]): A = opt.getOrElse(default)
}

object Default2 {
/*
  def getDefaultString(): Default2 = new Default2{
    def default[String]: String = "deaultValue"
  }

  def getDefaultInt(): Default2 = new Default2{
    def default[MAO]: MAO = 0
  }
*/
  //def mao[G](opt: Option[G])(implicit default2: Default2): G = default.getOrDefault(opt)
}

trait ClassName {
  val name: String
}

object ClassName {
  val getStringClassName: ClassName = new ClassName {
    val name: String = "STring"
  }

  val getIntClassName: ClassName = new ClassName {
    val name: String = "Int"
  }
}

object Default {

  def mao[G](opt: Option[G])(implicit default: Default[G]): G = default.getOrDefault(opt)

  //def mao[G](???): ??? = ???
//  def mao[G](opt: Option[G])(implicit default: Default[G]): G = ???

  def getDefaultString(): Default[String] = new Default[String] {
    val default: String = "myDefaultString"
   // def getOrDefault(opt: Option[String]): String = mao(opt)// opt.getOrElse(default)
  }


  def getDefaultInt(): Default[Int] = new Default[Int] {
    val default: Int = 0
   // def getOrDefault(opt: Option[Int]): Int = mao(opt)// opt.getOrElse(default)
  }


}

object DefaultExperiment extends App {
  val opt: Option[String] = None
  implicit  val stringDefault: Default[String] = Default.getDefaultString()

  val result = Default.mao(opt)
  println(s"The result was: ${result}")

  val default2 : Default2 = ??? ///Default2.getDefaultInt()
  val s: String = default2.default[String]
  val b: Boolean = default2.default[Boolean]
  val i: Int = default2.default[Int]

//  implicit val stringClassName: ClassName = ClassName.getStringClassName()
//  implicit val intClassName: ClassName = ClassName.getIntClassName()

 // println(s"The int classname is ${implicitly[ClassName]}")
}
