package pie

import doodle.core._
import doodle.image._
import doodle.image.syntax._
import doodle.image.syntax.core._

sealed trait Handful[+A] {
  import Handful._
  def flatMap[B](f: A => Handful[B]): Handful[B] = sliceHandful(this, f)
  def map[B](f: A => B): Handful[B] = modifyHandful(this, f)
  def withFilter(f: A => Boolean): Handful[A] = foldHandful[A, Handful[A]](
    this,
    Handful.Empty(),
    (a, handful) =>
      if (f(a)) {
        Handful.Several(a, handful)
      } else {
        handful
      }
  )
}

object Handful {
  case class Empty[A]() extends Handful[A]
  case class Several[A](first: A, rest: Handful[A]) extends Handful[A]

  def modifyHandful[A, B](
      handful: Handful[A],
      f: A => B
  ): Handful[B] =
    foldHandful[A, Handful[B]](
      handful,
      Handful.Empty(),
      (first, handful) => Handful.Several(f(first), handful)
    )

  def sliceHandful[A, B](handful: Handful[A], f: A => Handful[B]): Handful[B] =
    foldHandful[A, Handful[B]](
      handful,
      Handful.Empty(),
      (a, handful) => {
        val bs = f(a)
        foldHandful[B, Handful[B]](
          bs,
          handful,
          (b, handful) => Handful.Several(b, handful)
        )
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

  def handfulToImage[A](scale: Int, handful: Handful[A])(
      using piece: Piece[A]
  ): Image = {
    val total = countHandful(handful)
    val (image, count) = foldHandful(
      handful,
      (Image.empty, total),
      { case (a, (image, n)) =>
        val point = piece.curve(scale, total, n)
        (image.on(piece.image(a).at(point)), n - 1)
      }
    )
    image
  }

}

trait Piece[A] {
  def image(a: A): Image
  def curve(scale: Int, total: Int, n: Int): Point
}
