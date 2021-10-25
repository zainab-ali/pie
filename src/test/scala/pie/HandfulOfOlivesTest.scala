package pie

import weaver._

object HandfulOfOlivesTest extends SimpleIOSuite {

  import Toppings._
  import Toppings.Olive._
  import Toppings.HandfulOfOlives._

  pureTest("A handful of a no olives is empty ") {
    expect(Toppings.grabHandfulOfOlives(0) == Empty)
  }

  pureTest("A handful of two olives contains two olives") {
    expect(Toppings.grabHandfulOfOlives(2) == Several(Kalamata, Several(Kalamata, Empty)))
  }

  pureTest("There are three olives in a handful of three olives") {
    expect(Toppings.countOlives(Toppings.grabHandfulOfOlives(3)) == 3)
  }

  pureTest("Toppings.toNicoise exchanges all olives in a handful for Nicoise olives") {
    expect(Toppings.toNicoise(Toppings.grabHandfulOfOlives(2)) == Several(Nicoise, Several(Nicoise, Empty)))
  }

  pureTest("Toppings.toNicoise exchanges olives with the same number of Nicoise olives") {
    expect(Toppings.countOlives(Toppings.toNicoise(Toppings.grabHandfulOfOlives(2))) == 2)
      .and(expect(Toppings.countOlives(Toppings.toNicoise(Toppings.grabHandfulOfOlives(5))) == 5))
      .and(expect(Toppings.countOlives(Toppings.toNicoise(Toppings.grabHandfulOfOlives(0))) == 0))
  }

  pureTest("Toppings.stuffWithPimento exchanges olives with the same number of Nicoise olives") {
    expect(Toppings.countOlives(Toppings.stuffWithPimento(Toppings.grabHandfulOfOlives(2))) == 2)
      .and(expect(Toppings.countOlives(Toppings.stuffWithPimento(Toppings.grabHandfulOfOlives(5))) == 5))
      .and(expect(Toppings.countOlives(Toppings.stuffWithPimento(Toppings.grabHandfulOfOlives(0))) == 0))
  }

  pureTest("Toppings.stuffWithPimento stuffs olives with pimento") {
    expect(Toppings.stuffWithPimento(Several(Kalamata, Empty)) == Several(PimentoStuffed(Kalamata, Pimento), Empty))
  }

  pureTest("Toppings.stuffWithPimento does not stuff pimento-stuffed olives") {
    expect(Toppings.stuffWithPimento(Several(PimentoStuffed(Kalamata, Pimento), Empty)) == Several(PimentoStuffed(Kalamata, Pimento), Empty))
  }
}
