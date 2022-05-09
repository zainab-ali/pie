package pie.core

import cats.data.NonEmptyList
import pie.italy.{Bologna, Core, ItalianSauce, Napoli, Pizza}
import pie.italy.PizzaShop.{correction, validateSize}

trait SauceParser[T] {
    def apply(sauce: String): Either[StrangeSauce.type, T]
}

object Validation {
    def parseSize(size: String): Either[NotASize.type, Int] =
        size.toIntOption.toRight(NotASize).map(_ * 10)

    def validatePizza[T](size: Int, sauce: String)(implicit sauceParser: SauceParser[T]): Either[NonEmptyList[PizzaError], Pizza] = {
        val eitherSizeOrError = validateSize(size) match {
            case Right(size) => Right(size)
            case Left(error) => correction(error)
        }
        val eitherSauceOrError = sauceParser(sauce)
        (eitherSizeOrError, eitherSauceOrError) match {
            case (Left(sizeError), Left(sauceError)) => Left(NonEmptyList(sizeError, List(sauceError)))
            case (Left(sizeError), Right(_)) => Left(NonEmptyList(sizeError, Nil))
            case (Right(_), Left(sauceError)) => Left(NonEmptyList(sauceError, Nil))
            case (Right(pizza), Right(sauce)) => Right(pizza.copy(size = size, sauce = sauce))
        }
    }
}
