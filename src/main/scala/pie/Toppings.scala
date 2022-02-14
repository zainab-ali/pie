package pie

import doodle.core._
import doodle.image._
import doodle.image.syntax._
import doodle.image.syntax.core._

class Toppings{
  ???
}

object Toppings {

  import Handful._

  object Pimento

  sealed trait Topping

  // An olive is either Nicoise, Kalamata or an olive stuffed with Pimento
  sealed trait Olive extends Topping

  object Olive {
    case object Nicoise extends Olive
    case object Kalamata extends Olive
    // Is there something odd about this definition of a pimento-stuffed olive? You don't need to address the problem.
    case class PimentoStuffed(olive: Olive, pimento: Pimento.type) extends Olive

    implicit val olivePiece: Piece[Olive] = new Piece[Olive] {
      println("Olive Piece Implicit")
      def image(olive: Olive): Image = addOliveColourToImage(olive)(oliveImage)
      def curve(scale: Int, total: Int, n: Int): Point =
        pointOfNthOlive(scale, total, n)
    }
  }

  def hamPieceFromImage(i: Image): Piece[Ham.type] =  new Piece[Ham.type] {
    def image(ham: Ham.type): Image = i

    def curve(scale: Int, total: Int, n: Int): Point =
      pointOfNthHam(scale, total, n)
  }

  val blackForestHamPiece = hamPieceFromImage(
    Image.circle(15).fillColor(Color.pink).strokeWidth(2)
  )

  val americanHamPiece = hamPieceFromImage(
    Image.square(15).fillColor(Color.pink).noStroke
  )

  val parmaHamPiece = hamPieceFromImage(
    Image.rectangle(5, 20).fillColor(Color.white)
      .beside(Image.rectangle(10, 20).fillColor(Color.pink.darken(0.2.normalized)))
      .noStroke
  )

  case object Ham extends Topping {
    implicit val hamPiece: Piece[Ham.type] = parmaHamPiece

  }

  class Banana(colour: Color)

  object Banana {
    implicit val bananaPiece: Piece[Banana] = ???
  }

  def modifyTheTopping(f: Topping => Topping): Topping = {
    val olive = Olive.Kalamata
    def foo(): Unit = {
      ///
    }
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
    for {
      _ <- handful
    } yield Olive.Nicoise

  //  modifyHandful( _ => Olive.Nicoise)
  def stuffWithPimento(handful: HandfulOfOlives): HandfulOfOlives =
    handful.map({
      case olive @ (Olive.Kalamata | Olive.Nicoise) =>
        Olive.PimentoStuffed(olive, Pimento)
      case olive: Olive.PimentoStuffed => olive
    })
//    modifyHandful(
//      handful,
//      {
//        case olive @ (Olive.Kalamata | Olive.Nicoise) =>
//          Olive.PimentoStuffed(olive, Pimento)
//        case olive: Olive.PimentoStuffed => olive
//      }

  case class OliveSlice(olive: Olive)

  def sliceOlive(olive: Olive): Handful[OliveSlice] = {
    grabHandful(3, OliveSlice(olive))
  }

  def sliceHandfulOfOlives(handful: Handful[Olive]): Handful[OliveSlice] =
    for {
      olive <- handful
      slice <- sliceOlive(olive)
    } yield slice
//    foldHandful[Olive, Handful[OliveSlice]](
//      handful,
//      Handful.Empty(),
//      (olive, handful) => {
//        val slices = sliceOlive(olive)
//        foldHandful[OliveSlice, Handful[OliveSlice]](
//          slices,
//          handful,
//          (slice, handful) => Handful.Several(slice, handful)
//        )
//      }
//    )

  def sliceHam(ham: Ham.type): Handful[Ham.type] =
    grabHandful(3, ham)

  def sliceHandfulOfHam(handful: Handful[Ham.type]): Handful[Ham.type] =
    handful.flatMap(ham => sliceHam(ham))
//    foldHandful[Ham.type, Handful[Ham.type]](
//      handful,
//      Handful.Empty(),
//      (ham, handful) => {
//        val slices = sliceHam(ham)
//        foldHandful[Ham.type, Handful[Ham.type]](
//          slices,
//          handful,
//          (slice, handful) => Handful.Several(slice, handful)
//        )
//      }
//    )




  def combineOlivesAndHam(olives: Handful[Olive], hams: Handful[Ham.type]): Handful[Topping] = {
    foldHandful[Topping, Handful[Topping]](
      olives,
      hams,
      (olive, handful) => Handful.Several(olive, handful)
    )
  }

  def pairOliveSlicesAndHam(olives: Handful[Olive], hams: Handful[Ham.type]): Handful[(OliveSlice, Ham.type)] = {
    olives.flatMap { olive =>
      sliceOlive(olive).flatMap { oliveSlice =>
        hams.map { ham =>
          (oliveSlice, ham)
        }
      }
    }


    for {
      olive <- olives
      oliveSlice <- sliceOlive(olive)
      ham <- hams
    } yield (oliveSlice, ham)
  }

  def pairOliveSlicesAndHamSlices(olives: Handful[Olive], hams: Handful[Ham.type]): Handful[(OliveSlice, Ham.type)] =
  {
    // for {
    //   (oliveSlice, ham) <- pairOliveSlicesAndHam(olives, hams)
    //   hamSlice <- sliceHam(ham)
    // } yield (oliveSlice, hamSlice)

    pairOliveSlicesAndHam(olives, hams)
      .withFilter({ case (_, _) => true})
      .flatMap { case (oliveSlice, ham) =>
        sliceHam(ham).map(hamSlice => (oliveSlice, hamSlice))
      }
  }

  def combineHandfuls[A](first: Handful[A], second: Handful[A]): Handful[A] = {
    foldHandful[A, Handful[A]](
      first,
      second,
      (a, handful) => Handful.Several(a, handful)
    )
  }

  def addOliveColourToImage(olive: Olive): Image => Image = olive match {
    case Olive.Nicoise  => _.fillColor(Color.green).scale(1.5, 1.5)
    case Olive.Kalamata => _.fillColor(Color.purple)
    case Olive.PimentoStuffed(olive, _) =>
      addOliveColourToImage(olive).andThen(addPimentoToImage)
  }

  val addPimentoToImage: Image => Image =
    image => image.scale(0.5, 0.5).fillColor(Color.darkRed).on(image)

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

  def handfulOfOlivesToImage(
      scale: Int,
      handfulOfOlives: HandfulOfOlives
  ): Image = {
    handfulToImage(scale, handfulOfOlives)
  }

  implicit val americanHamPieceImplicit: Piece[Ham.type] =  americanHamPiece

//  import germanPizzaImplicits._

  def handfulOfHamToImage(
      scale: Int,
      handfulOfHam: Handful[Ham.type]
  ): Image = {
    import germanPizzaImplicits._

    handfulToImage(scale, handfulOfHam)
  }



}

object germanPizzaImplicits {

  implicit val germanHamPiece: Piece[Toppings.Ham.type] = Toppings.blackForestHamPiece
}
