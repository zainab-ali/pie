package pie.italy

import doodle.core.Color
import doodle.image.Image
import pie.core.{Bechamel2, CoreSauce, FixedColorSauceToImage, SauceToImage, Tomato2}

sealed trait ItalianSauce

object ItalianSauce {

  implicit val sauceToImage: SauceToImage[ItalianSauce] =
    new SauceToImage[ItalianSauce] {
      override def toImage(sauce: ItalianSauce, size: Int): Image =
        Image.circle(size * 0.75).fillColor(color(sauce)).noStroke
    }

  def color(sauce: ItalianSauce): Color = sauce match {
    case Napoli => Color.orange
    case Bologna => Color.brown
    case Core(Tomato2) => Color.red
    case Core(Bechamel2) => Color.white
  }
}

object Napoli extends ItalianSauce {

  implicit val sauceToImage: FixedColorSauceToImage[Napoli.type] =
    new FixedColorSauceToImage[Napoli.type](Color.orange)
}

object Bologna extends ItalianSauce {

  implicit val sauceToImage: FixedColorSauceToImage[Bologna.type] =
    new FixedColorSauceToImage[Bologna.type](Color.brown)
}

final case class Core(core: CoreSauce) extends ItalianSauce
