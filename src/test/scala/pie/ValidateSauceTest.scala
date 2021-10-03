package pie

import weaver._

object ValidateSizeAndSauceTest extends SimpleIOSuite {

  pureTest("""A sauce of "white" evaluates to a bechamel pizza""") {
    unimplemented
  }

  pureTest("""A sauce of "red" evaluates to a tomato pizza""") {
    unimplemented
  }
  pureTest("""A sauce of "soy" evaluates to a StrangeSauce error""") {
    unimplemented
  }

  pureTest("""A size of -1 and a sauce of "soy" evaluates to a NegativeSize error and a StrangeSauce error""") {
    unimplemented
  }
}

