package pie.italy

import doodle.core.Color
import doodle.image.Image
import pie.core.CoreSauce


sealed trait ItalianSauce
object Napoli extends ItalianSauce {
  // override def sauceColor = Color.orange
  // override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke
}

object Bologna extends ItalianSauce {
  // override def sauceColor: Color = Color.brown
  // override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke
}
final case class Core(core: CoreSauce) extends ItalianSauce
