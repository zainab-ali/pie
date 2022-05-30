package pie.core

import cats.data.NonEmptyList
import pie.italy.{Bologna, Core, ItalianSauce, Napoli}
import pie.italy.PizzaShop.{ItalianPizza, correction, validateSize => italianValidateSize}

trait SauceParser[T] {
    def apply(sauce: String): Either[StrangeSauce.type, T]
}

object Validation {
    def parseSize(size: String): Either[NotASize.type, Int] =
        size.toIntOption.toRight(NotASize).map(_ * 10)


    def makePizzaBase(eitherErrorOrSize: Either[PizzaError, ValidSize]) = {
        eitherErrorOrSize match {
            case Left(err) => ???
            case Right(ValidSize.Three) => ???
            case Right(ValidSize.Four) => ???
            case Right(ValidSize.Five) => ???
            case Right(ValidSize.Six) => ???
        }
    }

    //def makeValidSize(size: Int): ValidSize = ???  // No
    def makeValidSize(size: Int): Option[ValidSize] = {
        ValidSize.values.find(_.size() == ValidSize.size())
    }

    def validateSize(size: Int): Either[PizzaError, ValidSize] = ???




    def validatePizza[T](size: Int, sauce: String)(implicit sauceParser: SauceParser[T]): Either[NonEmptyList[PizzaError], Pizza[T]] = {
        val eitherSizeOrError = italianValidateSize(size) match {
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
