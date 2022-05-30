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
    // === >> ture / false
    // PizzaError - we need a happy path
    // 5
    // Either[PizzaError, Unit] - promising!
    // Left / Right
    // Left <==> 5
    // () ==> 1 (4 to 15) == <<< 13 Size >>>
    // Option[PrizzaError]
    // >> None / Some(_)
    //          ==> Some == 5
    // 6

    trait ValidSize {
      def size(s: Int): Int
    }

    object ValidSize {
        case object Three extends ValidSize {
            override def size: Int = 3
        }
        case object Four extends ValidSize  {
            override def size: Int = 4
        }
        case object Five extends ValidSize {
            override def size: Int = 5
        }
        case object Six extends ValidSize {
            override def size: Int = 6
        }

        val values: Set[ValidSize] = Set(Three, Four, Five, Six)
    }

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

      if(ValidSize.values.contains(ValidSize))
        val x = ValidSize.values.toMap.exists()
            
      x
    }

    def makeInt(validSize: ValidSize): Int = ???

    def validateSize1(size: Int): Either[PizzaError, ValidSize] = {
        if (size < 0) Left(NegativeSize)
        else if (size <  3) Left(PizzaTooSmall)
        else if (size > 6) Left(PizzaTooBig)
        else if(size < 6 && size >3) Right(ValidSize.Three)
        else if(size == 4) Right(ValidSize.Four)
        else if (size ==5 )Right(ValidSize.Five)
        else Right(ValidSize.Six)
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
