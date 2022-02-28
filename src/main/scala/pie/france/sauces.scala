package pie.france

import pie.core.Sauce
import doodle.core.Color
import doodle.image.Image

object BlueCheese extends Sauce {
  override def sauceColor = Color.blue
  override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke
}

object Veloute extends Sauce {
  override def sauceColor: Color = Color.lightSalmon
  override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke
}