package pie

import doodle.core._
import doodle.image._
import doodle.image.syntax._
import doodle.image.syntax.core._

object Toppings {

  val oliveImage: Image = Image.circle(10).fillColor(Color.green)

  def pointOfNthOlive(scale: Int, totalNumberOfOlives: Int, n: Int): Point = {
    Point.polar(
      scale * 0.75 * 0.25,
      Angle.one / totalNumberOfOlives.toDouble * n.toDouble
    )
  }

  def olivesToImage(scale: Int, total: Int): Image = {
    def nthImage(n: Int): Image = n match {
      case 0 => Image.empty
      case n =>
        oliveImage
          .at(pointOfNthOlive(scale, total, n))
          .on(nthImage(n - 1))
    }
    nthImage(total)
  }

  def pointOfNthHam(scale: Int, totalNumberOfHamSlices: Int, n: Int): Point = {
    ???
  }

  val hamImage = Image.square(15).fillColor(Color.pink).noStroke

  def hamToImage(scale: Int, total: Int): Image = {
    def nthImage(n: Int): Image = n match {
      case 0 => ???
      case n => ???
    }
    nthImage(total)
  }

  def pointOfNthSweetcornInHandful(
      scale: Int,
      numberOfSweetcornsInRing: Int,
      radius: Double,
      n: Int
  ): Point = {
    ???
  }

  val sweetcornImage = Image.triangle(7, 7).fillColor(Color.yellow).noStroke

  def sweetcornHandfulToImage(scale: Int, total: Int, radius: Double): Image = {
    def nthSweetcorn(n: Int): Image = n match {
      case 0 => ???
      case n => ???
    }
    nthSweetcorn(total)
  }

  def sweetcornToImage(scale: Int, total: Int): Image = {
    def nthSweetcorn(n: Int): Image = ???
    nthSweetcorn(total)
  }

  def toppingToImage(
      piece: Image,
      scale: Int,
      total: Int,
      curve: (Int, Int, Int) => Point
  ): Image = ???
}
