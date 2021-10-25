package pie

import doodle.core._
import doodle.image._
import doodle.image.syntax._
import doodle.image.syntax.core._

object Toppings {

  def toppingToImage(piece: Image, scale: Int, total: Int, curve: (Int, Int, Int) => Point): Image = {
    def toppingToImageGo(n: Int): Image = n match {
      case 0 => Image.empty
      case n =>
        toppingToImageGo(n - 1)
          .under(piece.at(curve(scale, total, n)))
    }
    toppingToImageGo(total)
  }

  val oliveImage: Image = Image.circle(10).fillColor(Color.green)

  def pointOfNthOlive(scale: Int, totalNumberOfOlives: Int, n: Int): Point = {
      Point.polar(scale * 0.75 * 0.25, Angle.one / totalNumberOfOlives.toDouble * n.toDouble)
  }

  def olivesToImage(scale: Int, total: Int): Image = {
    toppingToImage(oliveImage, scale, total, pointOfNthOlive)
  }

  def pointOfNthHam(scale: Int, totalNumberOfHamSlices: Int, n: Int): Point = {
      Point.polar(scale * 0.75 * 0.5 * n.toDouble / totalNumberOfHamSlices.toDouble, (45 * n.toDouble).degrees)
  }

  val hamImage = Image.square(15).fillColor(Color.pink).noStroke

  def hamToImage(scale: Int, total: Int): Image = {
    toppingToImage(hamImage, scale, total, pointOfNthHam)
  }

  def pointOfNthSweetcornInHandful(radius: Double, scale: Int, numberOfSweetcornsInHandful: Int, n: Int): Point = {
    Point.polar(scale * 0.75 * radius, Angle.one / numberOfSweetcornsInHandful.toDouble * n.toDouble)
  }

  val sweetcornImage = Image.triangle(7, 7).fillColor(Color.yellow).noStroke

  def sweetcornHandfulToImage(scale: Int, total: Int, radius: Double): Image = {
    toppingToImage(sweetcornImage, scale, total, pointOfNthSweetcornInHandful(radius, _, _, _))
  }

  def sweetcornToImage(scale: Int, total: Int): Image = {
    val numberOfRings: Int = total / 6
    def radiusOfRing(n: Int): Double = 0.15 + (0.65 * 0.5 * n.toDouble / numberOfRings.toDouble)
    def sweetcornToImageGo(n: Int): Image = n match {
      case n if n <= 6 => sweetcornHandfulToImage(scale, n, 0.15)
      case n =>
        val factor = radiusOfRing(n / 6)
        sweetcornToImageGo(n - 6)
          .under(sweetcornHandfulToImage(scale, 6, factor))
    }
    sweetcornToImageGo(total)
  }
}
