package pie.core

import doodle.core._
import doodle.image._

trait Sauce

object Tomato extends Sauce
object Bechamel extends Sauce

object Sauce {
  def toImage(scale: Int, sauce: Sauce): Image = sauce match {
    case Tomato => Image.circle(scale * 0.75).fillColor(Color.red).noStroke
    case Bechamel => Image.circle(scale * 0.75)
        .fillColor(Color.green).noStroke
  }
}

