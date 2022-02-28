package pie.france

// import pie.core.Sauce
import doodle.core.Color
import doodle.image.Image
import pie.core.CoreSauce

// object BlueCheese extends Sauce {
//   override def sauceColor = Color.blue
//   override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke
// }

// object Veloute extends Sauce {
//   override def sauceColor: Color = Color.lightSalmon
//   override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke
// }

sealed trait FrenchSauce
object BlueCheese2 extends FrenchSauce
object Veloute2 extends FrenchSauce
final case class Core(core: CoreSauce) extends FrenchSauce
