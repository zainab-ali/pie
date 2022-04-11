package pie.core

import doodle.image.Image

object implicits {

  implicit final class SauceToImageOps[A](sauce: A) {
    def toImage(size: Int)(implicit sauceToImage: SauceToImage[A]): Image = sauceToImage.toImage(sauce, size)
 }
}
