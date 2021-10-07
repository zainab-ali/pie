package pie

import weaver._

object SizeCorrectionTest extends SimpleIOSuite {

  pureTest("""A sauce of "white" evaluates to a bechamel pizza""") {
    expect(PizzaShop.correction(PizzaTooSmall) == Right(Pizza(3, Tomato)))
  }

  pureTest("""A sauce of "red" evaluates to a tomato pizza""") {
    expect(PizzaShop.correction(PizzaTooBig) == Right(Pizza(16, Tomato)))
  }
  pureTest("""A sauce of "soy" evaluates to a StrangeSauce error""") {
    expect(PizzaShop.correction(NegativeSize) == Left(NegativeSize))
  }
}
