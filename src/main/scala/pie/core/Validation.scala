package pie.core

import cats.data.NonEmptyList
import pie.italy.{Bologna, Core, ItalianSauce, Napoli}
import pie.italy.PizzaShop.{ItalianPizza, correction => italianCorrection, validateSize => italianValidateSize}
import cats.implicits.*

trait SauceParser[T] {
    def apply(sauce: String): Either[StrangeSauce.type, T]
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

    def validateSize(size: Int): Either[PizzaError, ValidSize] =  makeValidSize(size) match{
        case Some(validSize) => Right(validSize)
        case None =>
            if (size < 0) Left(NegativeSize)
            else if (size < ValidSize.minSize) Left(PizzaTooSmall)
            else Left(PizzaTooBig)
    }

    def correction(error: PizzaError): Either[PizzaError, ValidSize] = error match {
      case PizzaTooSmall => Right(ValidSize.Three)
      case PizzaTooBig => Right(ValidSize.Six)
      case other => Left(other)
    }

    def validatePizza[T](size: Int, sauce: String)(implicit sauceParser: SauceParser[T]): Either[NonEmptyList[PizzaError], Pizza[T]] = {
        val eitherSizeOrError: Either[PizzaError, ValidSize] = validateSize(size)

        val eitherSauceOrError: Either[StrangeSauce.type, T] = sauceParser(sauce)

        (eitherSizeOrError, eitherSauceOrError) match {
            case (Left(sizeError: PizzaError), Left(sauceError)) => Left(NonEmptyList(sizeError, List(sauceError)))
            case (Left(sizeError), Right(_)) => Left(NonEmptyList(sizeError, Nil))
            case (Right(_), Left(sauceError)) => Left(NonEmptyList(sauceError, Nil))
            case (Right(size), Right(sauce)) => Right(Pizza[T](size.size * 100, sauce)) // TODO: the constant 100 needs re-written
        }
    }
}
