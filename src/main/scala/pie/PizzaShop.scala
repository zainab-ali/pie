package pie

import doodle.core._
import doodle.image._
import doodle.image.syntax._
import doodle.java2d._

object PizzaShop {

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

