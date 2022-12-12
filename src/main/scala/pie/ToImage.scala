package pie

import doodle.image._

trait ToImage[A] {
  def toImage(scale: Int, a: A): Image
}
