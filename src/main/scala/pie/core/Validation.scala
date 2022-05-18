package pie.core

import cats.data.NonEmptyList
import pie.italy.{Bologna, Core, ItalianSauce, Napoli}
import pie.italy.PizzaShop.{ItalianPizza, Pizza, correction, validateSize}

trait SauceParser[T] {
    def apply(sauce: String): Either[StrangeSauce.type, T]
}

object Validation {
    def parseSize(size: String): Either[NotASize.type, Int] =
        size.toIntOption.toRight(NotASize).map(_ * 10)

    // Either[PizzaError, Pizza[T]] - generic, but constructs a pizza
    // Boolean - doesn't construct Pizza, but we lose the error information
    // PizzaError - we need a happy path
    // Either[PizzaError, Unit] - promising!
    def validateSize1(size: Int): Either[PizzaError, Unit] =
        if (size < 0) Left(NegativeSize)
        else if (size < 3) Left(PizzaTooSmall)
        else if (size > 16) Left(PizzaTooBig)
        else {
            val defaultPizza: ItalianPizza = CorePizza[ItalianSauce](size, Core(Tomato2))
            Right(CorePizza(size, Core(Tomato2)))
        }

    def validatePizza[T](size: Int, sauce: String)(implicit sauceParser: SauceParser[T]): Either[NonEmptyList[PizzaError], Pizza[T]] = {
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
