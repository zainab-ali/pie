package pie

import doodle.core._
import doodle.image._
import doodle.image.syntax._
import doodle.image.syntax.core._

object Toppings {

  object Pimento

  // An olive is either Nicoise, Kalamata or an olive stuffed with Pimento
  sealed trait Olive

  object Olive {
    case object Nicoise extends Olive
    case object Kalamata extends Olive
    // Is there something odd about this definition of a pimento-stuffed olive? You don't need to address the problem.
    case class PimentoStuffed(olive: Olive, pimento: Pimento.type) extends Olive
  }

  // A handful of olives is either empty, or a single olive and another handful
  sealed trait HandfulOfOlives
  object HandfulOfOlives {
    case object Empty extends HandfulOfOlives
    case class Several(olive: Olive, rest: HandfulOfOlives)
        extends HandfulOfOlives
  }

  def grabHandfulOfOlives(n: Int): HandfulOfOlives = ???

  def toNicoise(handful: HandfulOfOlives): HandfulOfOlives = ???
  def stuffWithPimento(handful: HandfulOfOlives): HandfulOfOlives = ???

  def modifyHandfulOfOlives(
      handful: HandfulOfOlives,
      f: Olive => Olive
  ): HandfulOfOlives = ???

  def addOliveColourToImage(olive: Olive): Image => Image = ???

  val addPimentoToImage: Image => Image = ???

  def countOlives(handfulOfOlives: HandfulOfOlives): Int = ???

  def handfulOfOlivesToImage(
      scale: Int,
      handfulOfOlives: HandfulOfOlives
  ): Image = ???

  def toppingToImage(
      piece: Image,
      scale: Int,
      total: Int,
      curve: (Int, Int, Int) => Point
  ): Image = {
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
    Point.polar(
      scale * 0.75 * 0.25,
      Angle.one / totalNumberOfOlives.toDouble * n.toDouble
    )
  }

  def olivesToImage(scale: Int, total: Int): Image = {
    toppingToImage(oliveImage, scale, total, pointOfNthOlive)
  }

  def pointOfNthHam(scale: Int, totalNumberOfHamSlices: Int, n: Int): Point = {
    Point.polar(
      scale * 0.75 * 0.5 * n.toDouble / totalNumberOfHamSlices.toDouble,
      (45 * n.toDouble).degrees
    )
  }

  val hamImage = Image.square(15).fillColor(Color.pink).noStroke

  def hamToImage(scale: Int, total: Int): Image = {
    toppingToImage(hamImage, scale, total, pointOfNthHam)
  }

  def pointOfNthSweetcornInHandful(
      radius: Double,
      scale: Int,
      numberOfSweetcornsInHandful: Int,
      n: Int
  ): Point = {
    Point.polar(
      scale * 0.75 * radius,
      Angle.one / numberOfSweetcornsInHandful.toDouble * n.toDouble
    )
  }

  val sweetcornImage = Image.triangle(7, 7).fillColor(Color.yellow).noStroke

  def sweetcornHandfulToImage(scale: Int, total: Int, radius: Double): Image = {
    toppingToImage(
      sweetcornImage,
      scale,
      total,
      pointOfNthSweetcornInHandful(radius, _, _, _)
    )
  }

  def sweetcornToImage(scale: Int, total: Int): Image = {
    val numberOfRings: Int = total / 6
    def radiusOfRing(n: Int): Double =
      0.15 + (0.65 * 0.5 * n.toDouble / numberOfRings.toDouble)
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
