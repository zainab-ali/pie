package pie.core

import doodle.core.*
import doodle.image.*
// import pie.core.Tomato.sauceColor

// We can't make this "sealed"
// trait Sauce {
//   def sauceColor: Color
//   def toImage(size: Int): Image
// }

// object Tomato extends Sauce {
//   override def sauceColor = Color.red
//   implicit val sauceToImage: SauceToImage[Tomato.type] = new SauceToImage[Tomato.type] {
//     def toImage(size: Int): Image = Image.circle(size * 0.75).fillColor(sauceColor).noStroke
//   }


//   override def toImage(size: Int): Image =  Sauce.toImage[Tomato.type](size)(sauceToImage)


//   val numTomatoes: Int = 23
// }

// object Bechamel extends Sauce {
//   override def sauceColor = Color.white
//   override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke
// }

object Sauce {

  // def toImage(size: Int, sauce: Sauce): Image = sauce.toImage(size)
  def toImage[A](size: Int)(implicit sauceToImage: SauceToImage[A]): Image = sauceToImage.toImage(size)

}

sealed trait CoreSauce
object Tomato2 extends CoreSauce {

  implicit val sauceToImage: SauceToImage[Tomato2.type] = new SauceToImage[Tomato2.type] {
    def toImage(size: Int): Image = Image.circle(size * 0.75).fillColor(Color.red).noStroke
  }

}
object Bechamel2 extends CoreSauce
