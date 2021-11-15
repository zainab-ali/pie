package pie

import doodle.core._
import doodle.image._
import doodle.image.syntax._
import doodle.image.syntax.core._

object Toppings {

  object Pimento

  sealed trait Topping

  // An olive is either Nicoise, Kalamata or an olive stuffed with Pimento
  sealed trait Olive extends Topping

  object Olive {
    case object Nicoise extends Olive
    case object Kalamata extends Olive
    // Is there something odd about this definition of a pimento-stuffed olive? You don't need to address the problem.
    case class PimentoStuffed(olive: Olive, pimento: Pimento.type) extends Olive
  }

  case object Ham extends Topping

  def modifyTheTopping(f: Topping => Topping): Topping = {
    val olive = Olive.Kalamata
    f(olive)
  }

  def modifyTheOlive(f: Olive => Topping): Topping = {
    val olive = Olive.Kalamata
    f(olive)
  }

  def olivefyTheTopping(f: Topping => Olive): Topping = {
    val olive = Olive.Kalamata
    f(olive)
  }

  val exchangeTopping: Topping => Topping = {
     case o: Olive => Ham
     case Ham => Olive.Kalamata
  }

  val exchangeToppingForOlive: Topping => Olive = {
    _ => Olive.Kalamata
  }

  val exchangeOliveForTopping: Olive => Topping = {
    case Olive.Kalamata => Ham
    case o: Olive => o
  }

  // Question 1
  // modifyTheTopping(exchangeTopping)
  // modifyTheTopping(exchangeToppingForOlive)
  // modifyTheTopping(exchangeOliveForTopping)


  // Question 2
  // modifyTheOlive(exchangeTopping)
  // modifyTheOlive(exchangeToppingForOlive)
  // modifyTheOlive(exchangeOliveForTopping)

  // Question 3
  // olivefyTheTopping(exchangeTopping)
  // olivefyTheTopping(exchangeToppingForOlive)
  // olivefyTheTopping(exchangeOliveForTopping)



  sealed trait Handful[A]

  object Handful {
    case class Empty[A]() extends Handful[A]
    case class Several[A](first: A, rest: Handful[A]) extends Handful[A]
  }

  type HandfulOfOlives = Handful[Olive]

  val handfulOfHam: Handful[Ham.type] =
    Handful.Several(Ham, Handful.Several(Ham, Handful.Empty()))

  def grabHandful[A](n: Int, first: A): Handful[A] = n match {
    case 0 => Handful.Empty()
    case n =>
      Handful.Several(first, grabHandful(n - 1, first))
  }

  def grabHandfulOfHam(n: Int): Handful[Ham.type] =
    grabHandful(n, Ham)

  def grabHandfulOfOlives(n: Int): HandfulOfOlives =
    grabHandful(n, Olive.Kalamata)

  def toNicoise(handful: HandfulOfOlives): HandfulOfOlives =
    modifyHandful(handful, _ => Olive.Nicoise)
  def stuffWithPimento(handful: HandfulOfOlives): HandfulOfOlives =
    modifyHandful(
      handful,
      {
        case olive @ (Olive.Kalamata | Olive.Nicoise) =>
          Olive.PimentoStuffed(olive, Pimento)
        case olive: Olive.PimentoStuffed => olive
      }
    )

  def foldHandful[A, B](
      handful: Handful[A],
      base: B,
      combine: (A, B) => B
  ): B = {
    handful match {
      case Handful.Empty() => base
      case Handful.Several(first, rest) =>
        combine(first, foldHandful(rest, base, combine))
    }
  }

  def countHandful[A](handful: Handful[A]): Int =
    foldHandful(handful, 0, (_, count) => count + 1)

  def modifyHandful[A](
      handful: Handful[A],
      f: A => A
  ): Handful[A] =
    foldHandful[A, Handful[A]](
      handful,
      Handful.Empty(),
      (first, handful) => Handful.Several(f(first), handful)
    )

  def addOliveColourToImage(olive: Olive): Image => Image = olive match {
    case Olive.Nicoise  => _.fillColor(Color.green).scale(1.5, 1.5)
    case Olive.Kalamata => _.fillColor(Color.purple)
    case Olive.PimentoStuffed(olive, _) =>
      addOliveColourToImage(olive).andThen(addPimentoToImage)
  }

  val addPimentoToImage: Image => Image =
    image => image.scale(0.5, 0.5).fillColor(Color.darkRed).on(image)

  def handfulOfOlivesToImage(
      scale: Int,
      handfulOfOlives: HandfulOfOlives
  ): Image =
    handfulToImage(
      scale,
      handfulOfOlives,
      olive => addOliveColourToImage(olive)(oliveImage),
      pointOfNthOlive
    )

  def handfulToImage[A](
      scale: Int,
      handful: Handful[A],
      pieceImage: A => Image,
      curve: (Int, Int, Int) => Point
  ): Image = {
    val total = countHandful(handful)
    val (image, count) = foldHandful(
      handful,
      (Image.empty, total),
      { case (piece, (image, n)) =>
        val point = curve(scale, total, n)
        (image.on(pieceImage(piece).at(point)), n - 1)
      }
    )
    image
  }

  def handfulOfHamToImage(
      scale: Int,
      handfulOfHam: Handful[Ham.type]
  ): Image =
    handfulToImage(scale, handfulOfHam, _ => hamImage, pointOfNthHam)

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
