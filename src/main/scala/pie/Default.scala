package pie

import cats.ApplicativeError
import cats.effect.{IO, IOApp}
import pie.core.{PizzaError, StrangeSauce}

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

object Test extends IOApp.Simple {
  trait SauceParser[F[_], T] {
    def mao(sauce: String): F[T]
  }

  // 2
  trait SauceParserTwo[T] {
    def apply[F[_]](sauce: String): F[T]
  }

  // 3
  trait SauceParserThree[F[_], T] {
    def apply[F[_]](sauce: String): F[T]
  }

  // 4
  trait SauceParserFour[T] {
    def apply[F[_]](sauce: String)(implicit ae: ApplicativeError[F, StrangeSauce.type]): F[T]
  }

  val sp = new SauceParser[IO, String] {
    override def mao(sauce: String): IO[String] = IO.pure(sauce)
  }

//  val sp3 = new SauceParserThree[IO, String] {
//    override def apply[F[_]](sauce: String): F[String] = IO.pure(sauce)
//  }

  val sp4 = new SauceParserFour[String] {
    override def apply[F[_]](sauce: String)(implicit ae: ApplicativeError[F, StrangeSauce.type]): F[String] = ae.pure(sauce)
  }

//  val sp2 = new SauceParserTwo[String] {
//    override def apply[F[_]](sauce: String): F[String] = ??? //Doesn't work
//    def anotherMethod[A]: String = "???"
//  }

  import cats.implicits._
  import cats.effect.implicits._

  type AnotherEither[T] = Either[StrangeSauce.type, T]
//  val eitherApplicativeError: ApplicativeError[IO, PizzaError] = implicitly[ApplicativeError[IO, PizzaError]]

  override def run: IO[Unit] = sp4.apply[AnotherEither]("Something").liftTo[IO].void
}
