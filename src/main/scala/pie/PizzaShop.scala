package pie

import doodle.core._
import doodle.image._
import doodle.image.syntax._
import doodle.java2d._
import scala.util.Try

object PizzaShop {

  //type Pizza = (String, Topping)
  //type Topping = Either[String, String]

  final case class Pizza(size: Int, sauce: Sauce)
  sealed trait Sauce
  object Sauce {
    case object Bechamel extends Sauce
    case object Tomato extends Sauce
  }

  def main(args: Array[String]): Unit = {
    
    val pizza = Pizza(pizzaSize, Sauce.Tomato)

    val base = Image.circle(pizza.size)
      .fillColor(Color.beige)
    val sauce = base.scale(0.75, 0.75)
      .fillColor(
        if pizza.sauce == Sauce.Tomato then Color.red else Color.white)
    val olive = Image.circle(10)
      .fillColor(Color.green)

    val image = olive.on(sauce).on(base)

    image.draw()
  }
  def constPizza(maybeSize: String): Either[Error, Pizza] ={
    val maybePizzaSize = Try(maybeSize.toInt * 10)
    
    val maybePizza = maybePizzaSize.map(Pizza(_, Sauce.Tomato))

    maybePizza
  }
}

