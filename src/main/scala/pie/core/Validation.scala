package pie.core

import cats.data.NonEmptyList
import cats.{ApplicativeError, MonadError}
import cats.implicits.*
import cats.effect.implicits.*

import scala.annotation.implicitNotFound
@implicitNotFound("There's no implicit!")
trait SauceParser[F[_], T] {
    def apply(sauce: String): F[T]
}

object Validation {
    def parseSize(size: String): Either[NonIntSize.type, Int] = {
        size.toIntOption.toRight(NonIntSize)
    }


    def makeValidSize(size: Int): Option[ValidSize] = {
      println(s"Input Size :: $size")
        val maybeSize = ValidSize.values.find( v => {
          println(s"values :: $v")
          v.size == size
        }
        )
      println(s"maybeSize : $maybeSize")
      maybeSize
    }

  /**
   * ApplicativeError has the following signature: ApplicativeError[F[_], E]. Which of these types fit the bounds:
    ApplicativeError[Either, ValidSize] // Does not compile
    ApplicativeError[Option, PizzaError] // Compiles to success
    ApplicativeError[ValidSize, PizzaError] // Does not compile
    ApplicativeError[fs2.Stream, PizzaError] // Does not compile
    ApplicativeError[IO, PizzaError] // Compiles to success
   *
   *
   * @tparam T
   */

    type ApplicativeOption = ApplicativeError[Option, PizzaError]
    //type ApplicativeValidSize = ApplicativeError[ValidSize, PizzaError]
    //type ApplicaticeStream = ApplicativeError[fs2.Stream, PizzaError]
    type applicativeIO = ApplicativeError[cats.effect.IO, PizzaError]

    type Sarpong = Throwable
    type TestType[T] = Either[String, T]
    val eitherMonadError: MonadError[TestType, String] = MonadError.apply(implicitly)
    // val eitherMonadError: MonadError[TestType, String] = MonadError[TestType, String]
//
    type MyEither[T] = Either[PizzaError, T]
    //But method catsStdInstancesForEither in trait EitherInstances does not match type cats.ApplicativeError[pie.core.Validation.MyEither, Throwable].


  // RECAP:
  // ApplicativeError compiles when F has one hole only
  // The error E can only be Throwable (for IO!)
  // The error E can be PizzaError for Either[PizzaError, T]
  //  - It must be the left of the either
  // The hole in F isn't filled with the error type, it's filled with the value type

//    val imp = catsStdInstancesForEither[PizzaError]
    val eitherApplicativeError = implicitly[ApplicativeError[MyEither, PizzaError]]
    import cats.effect.IO
    val ioApplicativeError = implicitly[ApplicativeError[IO, Sarpong]] // IO[Throwable]

    val raiseString: IO[Boolean] = IO.raiseError(new Exception("BOOM!"))

    def validateSize[F[_]](size: Int)(implicit ae: ApplicativeError[F, PizzaError]): F[ValidSize] = makeValidSize(size) match {
        case Some(validSize) => validSize.pure[F]
        case None =>
            if (size < 0) {
              NegativeSize.raiseError[F, ValidSize]
              //(new cats.syntax.ApplicativeErrorIdOps[PizzaError](NegativeSize)).raiseError[MyEither, ValidSize](eitherApplicativeError)
              //cats.syntax.applicativeError.catsSyntaxApplicativeErrorId(NegativeSize).raiseError[MyEither, ValidSize]
            }
            else if (size < ValidSize.minSize) PizzaTooSmall.raiseError[F, ValidSize]
            else PizzaTooBig.raiseError[F, ValidSize]
    }

    def correction[F[_]](error: PizzaError)(implicit ae: ApplicativeError[F, PizzaError]): F[ValidSize] = error match {
      case PizzaTooSmall => ValidSize.Three.pure[F] //cats.syntax.applicative.catsSyntaxApplicativeId(ValidSize.Three).pure[F]
      case PizzaTooBig => ValidSize.Six.pure[F]
      case other => other.raiseError[F, ValidSize]
    }

    def validatePizza[F[_], T](size: Int, sauce: String)(implicit sauceParser: SauceParser[F, T], ae: ApplicativeError[F, PizzaError]): F[Pizza[T]] = {

      val eitherSizeOrError: F[ValidSize] = validateSize[F](size).handleErrorWith(correction)

      val eitherSauceOrError: F[T] = sauceParser(sauce)

      val result: F[Pizza[T]] = ae.map2(eitherSizeOrError, eitherSauceOrError)((validSize, sauce)=>  Pizza[T](validSize.size * 100, sauce))
      result
    }

    def fromEither[F[_], E, A](either: Either[E, A])(implicit ae: ApplicativeError[F, E]): F[A] = either match {
      case Right(value) => value.pure[F]
      case Left(err) => err.raiseError[F, A]
    }
}
