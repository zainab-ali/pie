package pie

import weaver._

object ForComprehensionTest extends SimpleIOSuite {

  import Toppings._
  import Toppings.Olive._
  import Toppings.Handful._

  pureTest("pairOliveSlicesAndHam pairs each olive slice with each ham") {
    val olives = Several(Kalamata, Several(Nicoise, Empty()))
    val ham = Several(Ham, Empty())

    val result =
      Several(
        (OliveSlice(Kalamata), Ham),
        Several(
          (OliveSlice(Kalamata), Ham),
          Several(
            (OliveSlice(Kalamata), Ham),
            Several(
              (OliveSlice(Nicoise), Ham),
              Several(
                (OliveSlice(Nicoise), Ham),
                Several(
                  (OliveSlice(Nicoise), Ham),
                  Empty()
                )
              )
            )
          )
        )
      )

    expect(pairOliveSlicesAndHam(olives, ham) == result)
  }

  pureTest(
    "pairOliveSlicesAndHamSlices pairs each olive slice with each ham slice"
  ) {

    val olives = Several(Kalamata, Empty())
    val ham = Several(Ham, Empty())

    val result =
      Several(
        (OliveSlice(Kalamata), Ham),
        Several(
          (OliveSlice(Kalamata), Ham),
          Several(
            (OliveSlice(Kalamata), Ham),
            Several(
              (OliveSlice(Kalamata), Ham),
              Several(
                (OliveSlice(Kalamata), Ham),
                Several(
                  (OliveSlice(Kalamata), Ham),
                  Several(
                    (OliveSlice(Kalamata), Ham),
                    Several(
                      (OliveSlice(Kalamata), Ham),
                      Several(
                        (OliveSlice(Kalamata), Ham),
                        Empty()
                      )
                    )
                  )
                )
              )
            )
          )
        )
      )

    expect(pairOliveSlicesAndHamSlices(olives, ham) == result)

  }

  pureTest("withFilter filters out values") {
    val olives = Several(Kalamata, Several(Nicoise, Empty()))
    val filtered = olives
      .flatMap(sliceOlive)
      .withFilter({
        case OliveSlice(Kalamata) => true
        case _                    => false
      })

    val result = Several(
      OliveSlice(Kalamata),
      Several(OliveSlice(Kalamata), Several(OliveSlice(Kalamata), Empty()))
    )

    expect(filtered == result)
  }
}
