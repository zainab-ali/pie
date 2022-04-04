package pie.core

import doodle.image.Image

// Sauce
//  Colour - Property
// Image
// Size - Property
trait SauceToImage[A] {
  def toImage(sauce: A, size: Int): Image

}
