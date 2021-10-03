package pie

import weaver.SimpleIOSuite

object ValidateSizeTest extends SimpleIOSuite {

  pureTest("An input size of 12 evaluates to a 12-inch pizza") {
    unimplemented
  }

  pureTest("""An input size of "ten" fails to compile""") {
    // You'll have some trouble writing this test. Discuss:
    //  - Why is it more difficult to write than other tests?
    //  - Is it possible to write it?
    //    If not, how can we be assured that the code is correct?
    unimplemented
  }

  pureTest("An input size of -1 evaluates to a NegativeSize error") {
    unimplemented
  }

  pureTest("An input size of 17 evaluates to a PizzaTooBig error") {
    unimplemented
  }

  pureTest("An input size of 2 evaluates to a PizzaTooSmall error") {
    unimplemented
  }
}

