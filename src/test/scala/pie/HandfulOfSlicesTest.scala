package pie

import weaver._

object HandfulOfSlicesTest extends SimpleIOSuite {

  import Toppings._
  import Handful._
  import Olive._

  pureTest("An olive is sliced into three slices") {
    expect(
      sliceOlive(Kalamata) == Several(
        OliveSlice(Kalamata),
        Several(OliveSlice(Kalamata), Several(OliveSlice(Kalamata), Empty()))
      )
    )
  }

  pureTest("Toppings.sliceHandfulOfOlives slices a handful of olives") {
    val handfulOfOlives: Handful[Olive] = Several(
      Kalamata,
      Several(
        Nicoise,
        Empty()
      )
    )
    val handfulOfSlices = Several(
      OliveSlice(Kalamata),
      Several(
        OliveSlice(Kalamata),
        Several(
          OliveSlice(Kalamata),
          Several(
            OliveSlice(Nicoise),
            Several(OliveSlice(Nicoise), Several(OliveSlice(Nicoise), Empty()))
          )
        )
      )
    )
    expect(sliceHandfulOfOlives(handfulOfOlives) == handfulOfSlices)
  }

  pureTest("Toppings.sliceHandfulOfHam slices a handful of ham") {
    val handfulOfHam: Handful[Ham.type] = Several(
      Ham,
      Several(
        Ham,
        Empty()
      )
    )
    val handfulOfSlices = Several(
      Ham,
      Several(
        Ham,
        Several(
          Ham,
          Several(
            Ham,
            Several(Ham, Several(Ham, Empty()))
          )
        )
      )
    )
    expect(sliceHandfulOfHam(handfulOfHam) == handfulOfSlices)
  }

  pureTest(
    "Toppings.combineOlivesAndHam combines a handful of olives and a handful of ham"
  ) {
    val handfulOfOlives: Handful[Olive] = Several(
      Kalamata,
      Several(
        Nicoise,
        Empty()
      )
    )
    val handfulOfHam: Handful[Ham.type] = Several(
      Ham,
      Several(
        Ham,
        Empty()
      )
    )
    val handfulOfToppings: Handful[Topping] = Several(
      Kalamata,
      Several(
        Nicoise,
        Several(
          Ham,
          Several(
            Ham,
            Empty()
          )
        )
      )
    )

    expect(combineOlivesAndHam(handfulOfOlives, handfulOfHam) == handfulOfToppings)
  }
}
