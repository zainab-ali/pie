package pie.italy

import cats.ApplicativeError
import cats.Applicative
import doodle.core.*
import doodle.image.*
import doodle.image.syntax.*
import doodle.java2d.*
import cats.data.*
import pie.core.{Bechamel2, SauceToImage, Tomato2}
import pie.*
import pie.core.implicits.*
import pie.core.Validation
import pie.core.{Pizza as CorePizza, *}
import pie.core.StrangeSauce


object PizzaShop {

  type ItalianPizza = CorePizza[ItalianSauce]
  type Pizza = CorePizza[ItalianSauce]

  def validateSize(size: Int): Either[PizzaError, Pizza] =
    if (size < 0) Left(NegativeSize)
    else if (size < 3) Left(PizzaTooSmall)
    else if (size > 16) Left(PizzaTooBig)
    else {
      val defaultPizza: ItalianPizza = CorePizza[ItalianSauce](size, Core(Tomato2))
      Right(CorePizza(size, Core(Tomato2)))
    }

  // CorePizza[ItlianSauce]
  // CorePizza[Core]
  // Core extends ItalianSauce

  def correction(error: PizzaError): Either[PizzaError, ItalianPizza] = error match {
    case PizzaTooSmall =>
      val tomatoPizza = CorePizza[ItalianSauce](3, Core(Tomato2))
      Right(tomatoPizza)
    case PizzaTooBig => Right(CorePizza(16, Core(Tomato2)))
    case other => Left(other)
  }

  def validateSauce(sauce: String): Either[StrangeSauce.type, ItalianSauce] =
    if (sauce == "white") Right(Core(Bechamel2))
    else if (sauce == "red") Right(Core(Tomato2))
    else if (sauce == "napoli") Right(Napoli)
    else if (sauce == "brown") Right(Bologna)
    // else if (sauce == "blue") Right(BlueCheese)
    // else if (sauce == "lightBrown") Right(Veloute)
    else Left(StrangeSauce)

  // This solution only uses pattern matching - it doesn't use a cats.data.Validated or any functions for handling Either
  // Try playing around with the following code:
  // > import cats.implicits._
  // > validateSize(size).handleErrorWith(correction).toValidatedNel
//  def validatePizza(size: Int, sauce: String): Either[NonEmptyList[PizzaError], Pizza] = {
//    val eitherSizeOrError = validateSize(size) match {
//      case Right(size) => Right(size)
//      case Left(error) => correction(error)
//    }
//    val eitherSauceOrError = validateSauce(sauce)
//    (eitherSizeOrError, eitherSauceOrError) match {
//      case (Left(sizeError), Left(sauceError)) => Left(NonEmptyList(sizeError, List(sauceError)))
//      case (Left(sizeError), Right(_)) => Left(NonEmptyList(sizeError, Nil))
//      case (Right(_), Left(sauceError)) => Left(NonEmptyList(sauceError, Nil))
//      case (Right(pizza), Right(sauce)) => Right(pizza.copy(size = size, sauce = sauce))
//    }
//  }

  def pizzaToImage(pizza: Pizza): Image = pizza match {
    case Pizza(size, sauce) =>
      val sauceImage: Image = sauce.toImage(size)
      val baseImage = Image.circle(size).fillColor(Color.beige)
      val handfulOfNicoiseOlives =
        Toppings.handfulOfOlivesToImage(
          size,
          Toppings.stuffWithPimento(
            Toppings.toNicoise(Toppings.grabHandfulOfOlives(3))
          )
        )
      val handfulOfKalamataOlives =
        Toppings.handfulOfOlivesToImage(size, Toppings.grabHandfulOfOlives(8))
      val handfulOfHam =
        Toppings.handfulOfHamToImage(size, Toppings.grabHandfulOfHam(8))
      Toppings
        .sweetcornToImage(size, 20)
        .on(handfulOfNicoiseOlives)
        .on(handfulOfKalamataOlives)
        .on(handfulOfHam)
        .on(sauceImage)
        .on(baseImage)
  }

  def main(args: Array[String]): Unit = {
    val eitherSizeOrError = Validation.parseSize(args(0)) // 50
    type MyEither[T] = Either[StrangeSauce.type, T] //TODO MyEither needs to have a type PizzaError
    val test = implicitly[Applicative[MyEither]]
    implicit val sauceParser = new SauceParser[MyEither, ItalianSauce]{
      override def apply(sauce: String): MyEither[ItalianSauce] = validateSauce(sauce)
    }
    // This solution uses only pattern matching.
    // If you like, experiment with using functions such as `flatMap`
    val eitherPizzaOrError: Either[Object, Pizza] = eitherSizeOrError match {
      case Right(size) =>
        val sauce = args(1)
        val a: MyEither[Pizza] = Validation.validatePizza(size, sauce)(sauceParser, implicitly[ApplicativeError[MyEither, PizzaError]])
        a
      case Left(error) =>
        Left(NonEmptyList(error, Nil))
    }

    eitherPizzaOrError match {
      case Right(pizza) => pizzaToImage(pizza).draw()
      case Left(errors) => println(s"Couldn't make pizza. There were errors: $errors")
    }
  }
}

