package pie

import weaver._
import cats.data._

object ValidateSauceTest extends SimpleIOSuite {

  pureTest("""A sauce of "white" evaluates to bechamel""") {
    expect(PizzaShop.validateSauce("white") == Right(Bechamel))
  }

  pureTest("""A sauce of "red" evaluates to a tomato pizza""") {
    expect(PizzaShop.validateSauce("red") == Right(Tomato))
  }
  pureTest("""A sauce of "soy" evaluates to a StrangeSauce error""") {
    expect(PizzaShop.validateSauce("soy") == Left(StrangeSauce))
  }

  pureTest("""A size of -1 and a sauce of "soy" evaluates to a NegativeSize error and a StrangeSauce error""") {
    expect(PizzaShop.validatePizza(-1, "soy") == Left(NonEmptyList.of(NegativeSize, StrangeSauce)))
  }
}
