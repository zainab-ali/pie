package pie.italy

import doodle.core.Color
import doodle.image.Image
import pie.core.{CoreSauce, FixedColorSauceToImage, SauceToImage}


sealed trait ItalianSauce
object Napoli extends ItalianSauce {
  // override def sauceColor = Color.orange
  // override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke

  implicit val sauceToImage: FixedColorSauceToImage[Napoli.type] = new FixedColorSauceToImage[Napoli.type](Color.orange)
}

object Bologna extends ItalianSauce {
  // override def sauceColor: Color = Color.brown
  // override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke

  implicit val sauceToImage: FixedColorSauceToImage[Bologna.type] = new FixedColorSauceToImage[Bologna.type](Color.brown)
}
final case class Core(core: CoreSauce) extends ItalianSauce {
//  def toImage[A](size: Int)(implicit sauceToImage: SauceToImage[A]): Image = sauceToImage.toImage(size)

}

object Core {
//  implicit val sauceToImage: FixedColorSauceToImage[Core] = ??? // new FixedColorSauceToImage[Bologna.type](Color.brown)
}


