package pie.core

import doodle.core.*
import doodle.image.*

object Sauce {

  def toImage[A](sauce: A, size: Int)(implicit sauceToImage: SauceToImage[A]): Image =
    sauceToImage.toImage(sauce, size)
}

final class FixedColorSauceToImage[A](color: Color) extends SauceToImage[A] {
  def toImage(sauce: A, size: Int): Image =
    Image.circle(size * 0.75).fillColor(color).noStroke
}

sealed trait CoreSauce
object Tomato2 extends CoreSauce {

  implicit val sauceToImage: FixedColorSauceToImage[Tomato2.type] =
    new FixedColorSauceToImage[Tomato2.type](Color.red)
}
object Bechamel2 extends CoreSauce {
  implicit val sauceToImage: FixedColorSauceToImage[Bechamel2.type] =
    new FixedColorSauceToImage[Bechamel2.type](Color.white)
}
