package pie.core

import doodle.image.Image

trait SauceToImage[A] {
  def toImage(size: Int): Image

}


