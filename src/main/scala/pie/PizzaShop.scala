package pie

import doodle.core._
import doodle.image._
import doodle.image.syntax._
import doodle.java2d._

import cats.data._


final case class Pizza(size: Int, sauce: Sauce)

sealed trait Sauce
case object Tomato extends Sauce
case object Bechamel extends Sauce

sealed trait PizzaError
case object NotASize extends PizzaError
case object NegativeSize extends PizzaError
case object PizzaTooBig extends PizzaError
case object PizzaTooSmall extends PizzaError
case object StrangeSauce extends PizzaError

object PizzaShop {


  def parseSize(size: String): Either[NotASize.type, Int] =
    size.toIntOption.toRight(NotASize).map(_ * 10)

  def validateSize(size: Int): Either[PizzaError, Pizza] =
    if (size < 0) Left(NegativeSize)
    else if (size < 3) Left(PizzaTooSmall)
    else if (size > 16) Left(PizzaTooBig)
    else Right(Pizza(size, Tomato))

  def correction(error: PizzaError): Either[PizzaError, Pizza] = error match {
    case PizzaTooSmall => Right(Pizza(3, Tomato))
    case PizzaTooBig => Right(Pizza(16, Tomato))
    case other => Left(other)
  }

  def validateSauce(sauce: String): Either[PizzaError, Sauce] =
    if (sauce == "white") Right(Bechamel)
    else if (sauce == "red") Right(Tomato)
    else Left(StrangeSauce)

  // This solution only uses pattern matching - it doesn't use a cats.data.Validated or any functions for handling Either
  // Try playing around with the following code:
  // > import cats.implicits._
  // > validateSize(size).handleErrorWith(correction).toValidatedNel
  def validatePizza(size: Int, sauce: String): Either[NonEmptyList[PizzaError], Pizza] = {
    val eitherSizeOrError = validateSize(size) match {
      case Right(size) => Right(size)
      case Left(error) => correction(error)
    }
    val eitherSauceOrError = validateSauce(sauce)
    (eitherSizeOrError, eitherSauceOrError) match {
      case (Left(sizeError), Left(sauceError)) => Left(NonEmptyList(sizeError, List(sauceError)))
      case (Left(sizeError), Right(_)) => Left(NonEmptyList(sizeError, Nil))
      case (Right(_), Left(sauceError)) => Left(NonEmptyList(sauceError, Nil))
      case (Right(pizza), Right(sauce)) => Right(pizza.copy(size = size, sauce = sauce))
    }
  }

  val oliveImage: Image = ???

  def olivesToImage(size: Int, n: Int): Image = n match {
    case 0 => ???
    case n => ???
  }

  def sauceToImage(size: Int, sauce: Sauce): Image = ???

  def pizzaToImage(pizza: Pizza): Image = ???

  def main(args: Array[String]): Unit = {
    val eitherPizzaOrError: Either[NonEmptyList[PizzaError], Pizza] = ???

    eitherPizzaOrError match {
      case Right(pizza) => pizzaToImage(pizza).draw()
      case Left(errors) => println(s"Couldn't make pizza. There were errors: $errors")
    }
  }
}

