package pie

import weaver.SimpleIOSuite

object ValidateSizeTest extends SimpleIOSuite {

  pureTest("An input size of 12 evaluates to a 12-inch pizza") {
    expect(PizzaShop.validateSize(12) == Right(Pizza(12, Tomato)))
  }

  pureTest("""An input size of "ten" fails to compile""") {
    // You'll have some trouble writing this test. Discuss:
    //  - Why is it more difficult to write than other tests?
    //  - Is it possible to write it?
    //    If not, how can we be assured that the code is correct?
    unimplemented
  }

  pureTest("An input size of -1 evaluates to a NegativeSize error") {
    expect(PizzaShop.validateSize(-1) == Left(NegativeSize))
  }

  pureTest("An input size of 17 evaluates to a PizzaTooBig error") {
    expect(PizzaShop.validateSize(17) == Left(PizzaTooBig))
  }

  pureTest("An input size of 2 evaluates to a PizzaTooSmall error") {
    expect(PizzaShop.validateSize(2) == Left(PizzaTooSmall))
  }
}

