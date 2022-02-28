package pie.italy

import doodle.core.Color
import doodle.image.Image
import pie.core.Sauce


object Napoli extends Sauce {
  override def sauceColor = Color.orange
  override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke
}

object Bologna extends Sauce {
  override def sauceColor: Color = Color.brown
  override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke
}