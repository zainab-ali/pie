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

  def validateSize(size: Int): Either[PizzaError, Pizza] = ???

  def correction(error: PizzaError): Either[PizzaError, Pizza] = ???

  def validateSauce(sauce: String): Either[PizzaError, Sauce] = ???

  def validatePizza(size: Int, sauce: String): Either[NonEmptyList[PizzaError], Pizza] = ???

  def main(args: Array[String]): Unit = {
    val pizzaSize = args(0).toInt * 10

    val base = Image.circle(pizzaSize)
      .fillColor(Color.beige)
    val sauce = base.scale(0.75, 0.75)
      .fillColor(Color.red)
    val olive = Image.circle(10)
      .fillColor(Color.green)

    val image = olive.on(sauce).on(base)

    image.draw()
  }
}

