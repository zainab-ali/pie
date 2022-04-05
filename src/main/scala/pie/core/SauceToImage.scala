package pie.core

import doodle.image.Image

trait SauceToImage[A] {
  def toImage(sauce: A, size: Int): Image

}
