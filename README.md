# Pie

This is a tutorial on functional programming in Scala with cats, cats-effect and fs2.

Over the course of the tutorial you'll learn about:
 - Algebraic data types
 - Structural recursion
 - Typeclasses
 - Semigroups, monoids, functors and monads
 - Monad transformers
 - Free encodings
 - Effects and evaluation

## Getting Started

1. Type `sbt` to enter the sbt console.
2. Run the application with `run 12`.
   This should draw a 12 inch pizza with a single olive (you'll need to use your imagination here).

Take a look inside the `pie.PizzaShop` to see what gets run.

## Algebraic Data Types

Make sure you're familiar with [Modelling Data with Traits](https://books.underscore.io/essential-scala/essential-scala.html#modelling-data-with-traits)

### Basic validation with the Either datatype

Take a look at the tests in `ValidateSizeTest`. Write a function `validateSize` that:
 - takes a pizza size as an argument
 - evaluates to either a pizza or an error

The error must be one of:
 - `NegativeSize`
 - `PizzaTooBig`
 - `PizzaTooSmall`

*Discuss*: Should this really evaluate to a pizza? Could there be a better type to use.

### Error handling with the Either datatype

Write a function `correction`. As an argument, it should take in an error.
 - If the error is a `PizzaTooBig` error, it evaluates to a 16 inch pizza
 - If the error is a `PizzaTooSmall` error, it evaluates to a 3 inch pizza
 - If the error is none of the above, it evaluates to that same error

### Validation with the cats Validated datatype

Write a function `validateSauce` that:
 - takes a sauce name as an argument
 - evaluates to either `Bechamel`, `Tomato` or a `StrangeSauce` error

Use a `Validated` to evaluate both `vaildateSize` and `validateSauce`.

## Structural recursion

1. Write a `sauceToImage` function that evaluates to an image. The image
 - should be a circle, three quarters the diameter of the base (7.5 times the pizza size)
 - should be red if the sauce is `Tomato`
 - should be white if the sauce is `Bechamel`

```
def sauceToImage(size: Int, sauce: Sauce): Image = ???
```

It should look like this:

Write a `oliveImage` value that evaluates to an image. The image
 - should be a circle of diameter 10
 - should be green

```
val oliveImage: Image = ???
```


Write a `pizzaToImage` function that evaluates to an image. The image
 - should contain a base. This is a beige circle of 10 times the given pizza `size` 
 - should have the sauce image on top of it (use the `sauceToImage` function you've just written)
 - should have a single olive on top of it
 
 
Use `parseSize`, `validatePizza` and `pizzaToImage` in your `main` method. It should:
 - draw a valid pizza
 - print any errors to the console using `println`

### Recursion

Familiarize yourself with Doodle by reading [Creative Scala's Computing With Pictures chapter](https://www.creativescala.org/creative-scala.html#computing-with-pictures). 
In particular, experiment with `at` and `rotate`.

Write a recursive `olivesToImage` function that:
 - accepts a size
 - accepts a number of olives
 - evaluates to an image with the given number of olives arranged in a circle over the pizza sauce
 
Use `olivesToImage(size, 4)` in your `pizzaToImage` function to create pizzas with four olives.

Given a size of `12` and a sauce of `"red"`, this should display:
 
![A pizza with four olives](/images/structural-recursion-four-olive-pizza.png "A pizza with four olives")

### Higher order functions

Take a look at the `olivesToImage` function in `Toppings.scala`. It uses recursion by induction to construct an image of olives. The position of the nth olive is calculated using `pointOfNthOlive`.

1. Ham is sprinkled in a spiral.
 - Write a function `pointOfNthHam` that calculates the point of the nth slice of ham.
 - A ham slice is a pink square of length 15.
 - Write a function `hamToImage` that results in a spiral of ham.

2. A handful of sweetcorn is sprinked in a circle.
 - Write a function `pointOfNthSweetcornInHandful` that calculates the point of the nth sweetcorn in the handful.
 - A sweetcorn piece is a yellow triangle of width 5 and height 7.
 - Write a function `sweetcornHandfulToImage` that, given a radius, results in a circle of sweetcorn.
3. A handful contains up to six pieces of sweetcorn. Handfuls are sprinkled in concentric circles.
 - Write a function `sweetcornToImage` that results in concentric circles of sweetcorn handfuls
Given 8 olives, 5 pieces of ham, and 20 pieces of sweetcorn, this should display something like:
![A pizza with eight olives, five pieces of ham and twenty pieces of sweetcorn](/images/recursion-pizza.png "A pizza with eight olives, five pieces of ham and twenty pieces of sweetcorn")


4. Do you notice something similar about `hamToImage`, `oliveToImage` and `sweetcornToImage`?
Write a function `toppingToImage` that:
  - accepts an image for a `piece`
  - accepts a `curve` function that determines the point of the nth piece
Refactor `hamToImage`, `oliveToImage` and `sweetcornHandfulToImage` to use it

### Recap: recursion, induction and higher order functions

Examine the `Olive` and `HandfulOfOlives` algebraic data types in `Toppings.scala`. They have corresponding tests in `HandfulOfOlivesTest`

1. Write a `grabHandfulOfOlives` function that constructs a handful of `Kalamata` olives.
2. Write the `toNicoise` and `stuffWithPimento` functions. They should use the higher order function `modifyHandfulOfOlives` (you'll need to write this too).
3. Write an `addPimentoToImage` value that results in a function. The resulting function should add a small red circle to an image.
   Is there any difference between this value and the following declaration?

   ```scala
   def addPimentoToImage(image: Image): Image = ???
   ```

3. Write an `addOliveColourToImage` function that accepts an `olive` and results in a function that colours an image. The resulting function should give:
 - `Kalamata` olives a colour of purple.
 - `Nicoise` olives a colour of green and a larger size
 - `PimentoStuffed` olives a small red circle on top of them using the `addPimentoToImage` function.
4. Write a `countOlives` function that counts the number of olives in a handful.
5. Write a `handfulOfOlivesToImage` function that results in an image of olives arranged in a circle.
   Make use of the `countOlives` function and previous `pointOfNthOlive` function.

Your final pizza should look a bit like this:

![A pizza with pimento-stuffed olives](/images/higher-order-functions-pizza.png "A pizza with pimento stuffed olives")

## Generics

### Generic folds

1. Refresh yourself on folds by writing the `fold` function in `Toppings.scala`.
2. Use `fold` in `modifyHandfulOfOlives` and `countOlives`. These should no longer use explicit recursion (the recursive call occurs within `fold`).
3. Use `fold` in `handfulOfOlivesToImage` instead of the nested recursive function. This will require a little more thought.

### Generic algebraic data types

1. Write a `grabHandfulOfHam` function that constructs a handful of a given number of ham pieces. This function is used in `PizzaShop.scala`.
2. Take a look at `handfulOfHamToImage` It uses the functions `countHandful` and `foldHandful`.
   Write the signatures and definitions for these functions. `foldHandful` is the only function that should recurse.
3. Check that you can now compile and run the application.
4. We'll now do some refactoring.
   - Delete the `HandfulOfOlives` trait and use the following type alias:

     ```scala
     type HandfulOfOlives = Handful[Olive]
     ```
   - Delete the `countOlives` function and replace its usages with `countHandful`.
   - Delete the `fold` function and replace its usages with `foldHandful`.
   - Rewrite the `grabHandfulOfOlives` function to construct a `Handful[Olive]`.

   Check that your code compiles once again.

5. Examine `grabHandfulOfHam` and `grabHandfulOfOlives`. Do these have any shared code?
   Write a function `grabHandful` that grabs a generic handful.

6. Examine `modifyHandfulOfOlives`. Does this use any properties of `Olive`?
   Write a function `modifyHandful` that modifies a generic handful.

7. Finally, examime `handfulOfHamToImage` and `handfulOfOlivesToImage`. Do these have any shared code?
   Write a function `handfulToImage` that constructs an image from a generic handful.


## Variance

Take a look at the functions `modifyTheTopping`, `modifyTheOlive` and `olivefyTheTopping`. We'll pass these the functions `exchangeTopping`, `exchangeToppingForOlive` and `exchangeOliveForTopping`.

1. Which of these calls will compile? If so, what will their result be? (Try and predict the result before uncommenting the lines).

   ```scala
   modifyTheTopping(exchangeTopping)
   modifyTheTopping(exchangeToppingForOlive)
   modifyTheTopping(exchangeOliveForTopping)
   ```

   `Olive` is a subtype of `Topping`. What is the relationship between the type `Topping => Topping` and the type `Topping => Olive` (is one a subtype of the other)?

2. Which of these will compile? If so, what will their result be?

   ```scala
   modifyTheOlive(exchangeTopping)
   modifyTheOlive(exchangeToppingForOlive)
   modifyTheOlive(exchangeOliveForTopping)
   ```

   What is the relationship between `Topping => Topping` and `Olive => Topping`?

3. Now consider `olivefyTheTopping`. Which of these will compile? If so, what will their result be?

   ```scala
   olivefyTheTopping(exchangeTopping)
   olivefyTheTopping(exchangeToppingForOlive)
   olivefyTheTopping(exchangeOliveForTopping)
   ```

   What is the relationship between `Topping => Topping` and `Topping => Olive`?

4. The type `Olive => Topping` is a type alias for `Function1[Olive, Topping]`. The type `Function1` is a type constructor with two parameters: `Function1[I, O],` where `I` is the input parameter and `O` is the output result.

   - Read the definitions of invariance, covariance and contravariance in [Essential Scala](https://books.underscore.io/essential-scala/essential-scala.html#invariance-covariance-and-contravariance).

   - Consider the relationship between `Function1[Topping, Topping]`, `Function1[Olive, Topping]` and `Function1[Topping, Olive]`.
     Is `Function1` covariant or contravariant in `I`? Is it covariant or contravariant in `O`?


## Collections

An olive can be sliced into slices. Each slice has all the properties of its olive and is defined as:

```scala
case class OliveSlice(olive: Olive)
```

1. Write a function `sliceOlive` that slices an olive into a handful of three pieces:

2. Now write a function `sliceHandfulOfOlives` that slices a handful of olives and produces a handful of slices.
   You can use the `foldHandful` function to implement it.

3. A slice of ham can be sliced into more ham.

   a. Write a function `sliceHam` that slices a `Ham` into three pieces of `Ham`.
   b. Now write a function `sliceHandfulOfHam` that slices a handful of `Ham`.

4. What are the similarities betweeen `sliceHandfulOfHam` and `sliceHandfulOfOlives`? Write a generic `sliceHandful` function to share those similarities.

5. Write a function `combineOlivesAndHam` that combines a handful of olives and a handful of ham into a handful of toppings.
   Use the `foldHandful` function to implement it. Do you need to consider the variance of `Handful`?

6. Can you write a generic `combineHandfuls` function that combines a handful of one type with a handful of a different type?
   If so, what would its signature be?

## Lists

Take a look at [the API documentation for List](https://www.scala-lang.org/files/archive/api/2.13.7/scala/collection/immutable/List.html).

Handfuls are quite similar to the Scala `List`. For example, the function `foldHandful` has a similar signature to both the list `foldLeft` and `foldRight` functions.

1. `sliceHandful` has the following signature:

   ```scala
   def sliceHandful[A, B](handful: Handful[A], f: A => Handful[B]): Handful[B]
   ```
   What function on lists has the most similar signature? Does it have a similar behaviour?

2. What function on handful has the most similar signature and behaviour to [`++`](https://www.scala-lang.org/files/archive/api/2.13.7/scala/collection/immutable/List.html#++[B%3E:A](suffix:scala.collection.IterableOnce[B]):CC[B])?

3. Is there a function that has a similar signature to `modifyHandful`?
   If not, why?

## For comprehensions

1. Write the `toNicoise` function using `for` comprehension instead of `map`:

   Which version do you prefer?

2. Write the `sliceHandfulOfOlives` function using `for` comprehension instead of `flatMap`.

   Which version do you prefer?

3. Implement the function `withFilter` to pass the new test in `ForComprehensionTest`.

   *HINT:* You will need to use `foldHandful`.

4. You can now rewrite `pairOliveSlicesAndHamSlices` to pattern match on the tuple:

   ```scala
      	for {
      	  (oliveSlice, ham) <- pairOliveSlicesAndHam(olives, hams)
      	  hamSlice <- sliceHam(ham)
      	} yield (oliveSlice, hamSlice)
   ```

   Write the code that this desugars to using `withFilter`, `flatMap` and `map`.

   What function is passed to `withFilter`?

5. Rewrite the following code in `ForComprehensionTest` to use `for` comprehension:

   ```scala
    val filtered = olives
      .flatMap(sliceOlive)
      .withFilter({
        case OliveSlice(Kalamata) => true
        case _                    => false
      })

   ```

# Implicits and typeclasses

The code has several implicit `Piece[Ham.type]` instances, each with different images: `blackForestHam`, `parmaHam` and `americanHam`.

1. Without running the application, predict which ham will be drawn on the pizza.

   Run the application with `run 120 white`. Was the ham you expected drawn?

2. Will the following code change draw the `blackForestHam` type?

```diff
  implicit val americanHamPieceImplicit: Piece[Ham.type] =  americanHamPiece

+ import germanPizzaImplicits._

  def handfulOfHamToImage(
      scale: Int,
      handfulOfHam: Handful[Ham.type]
  ): Image = {
```

3. What about the following code change?

```diff
   def handfulOfHamToImage(
       scale: Int,
       handfulOfHam: Handful[Ham.type]
   ): Image = {
+    import germanPizzaImplicits._
     handfulToImage(scale, handfulOfHam)
   }

```

4. Consider the function `sweetcornHandfulToImage`. This is currently implemented using `toppingToImage`.

    a. Can you construct an instance of `Piece` for a `Sweetcorn` using the `sweetcornImage` and `pointOfNthSweetcornInHandful` functions?
       If not, why?

    b. Is there a way of writing `sweetcornHandfulToImage` using `handfulToImage`?


# Open and closed classes

Your pizza shop has expanded to france and italy. While still having `Tomato` and `Bechamel` sauces, each country has a specialty sauce. The `Sauce` data type has been split into the following files:

 - `core/Sauce.scala`
 - `france/sauces.scala`
 - `italy/sauces.scala`

The `toImage` function is defined as follows:

```scala
object Sauce {
  def toImage(scale: Int, sauce: Sauce): Image = sauce match {
    case Tomato => Image.circle(scale * 0.75).fillColor(Color.red).noStroke
    case Bechamel => Image.circle(scale * 0.75)
        .fillColor(Color.green).noStroke
  }
}
```

1. Try and draw the italian `Napoli` sauce:

   ```sh
   run 120 napoli
   ```

    - What went wrong?
    - Fix the error by adding a clause to `toImage` for `Napoli` that draws `Color.orange` sauce.
    - Repeat this for the french `BlueCheese` sauce. `run 120 blue` should draw a `Color.blue` sauce.

2. Is it possible for `Sauce` to be sealed? If not, why?

3. Add a `Bologna` sauce to `italy/sauces.scala`. It should be brown in colour.
   
   - Check that you can run the application with `run 120 bologna`.
   - Aside from the `italy/sauces.scala` file, where did you need to make code changes?

4. Next, we'll experiment with adding a `toImage` function to the `Sauce` trait:

   ```scala
   trait Sauce {
     def toImage(size: Int): Image
   }
   
   object Sauce {
     def toImage(size, sauce: Sauce): Image = sauce.toImage(size)
   }
   ```

    - Implement this function for each subtype of `Sauce`.

    - Add a light brown `Veloute` sauce to the `france/sauces.scala` file. Aside from the `france/sauces.scala` file, where do you need to make code changes?

5. Now consider the typeclass:

   ```scala
   trait SauceToImage[A]
     def toImage(size: Int): Image
   }
   
   object Sauce {
     def toImage[A](size)(implicit val sauceToImage: SauceToImage[A]): Image =
       sauceToImage.toImage(size)
   }
   ```
   
   Implement this typeclass for each subtype of `Sauce`.
   
   As an example, here's how to implement it for `Tomato`:
   
   ```scala
   object Tomato {
     implicit val sauceToImage: SauceToImage[Tomato.type] = new SauceToImage[Tomato.type] {
       def toImage(size: Int): Image = Image.circle(scale * 0.75).fillColor(Color.red).noStroke
     }
   }
   ```

6. The signature of `Sauce.toImage` is now different. Can the function be used in the same way by the `PizzaShop`?

7. Remove the `trait Sauce` and the `extends Sauce` statement from each sauce. Should the code compile?

# Ad-hoc polymorphism

There have been a few changes to the codebase:
 - The `Sauce` trait has now been removed in favour of the `CoreSauce` algebraic data type.
 - The `PizzaShop` only handles italian pizzas: it has been moved to the `italy` package.

1. Create instances of the `SauceToImage` typeclass for `Bechamel2`, `Napoli` and `Bologna`. Use these in place of the `???` in `PizzaShop`.

2. Try and create a `SauceToImage` instance for `ItalianSauce`. How can you use this in the `PizzaShop`?

3. Ultimately, we want to open a `PizzaShop` in France. Copy the `PizzaShop` to the `france` package and replace the `ItalianSauce` with `FrenchSauce`.
	- What algebraic data types are duplicated?
	- How would you reduce the duplication?

## Ad-hoc polymorphism: Typeclass instances

We've created a `SauceToImage` typeclass instance for `ItalianSauce`, however it is incorrect: all Italian sauces are blue.

```scala
implicit val sauceToImage: FixedColorSauceToImage[ItalianSauce] =  new FixedColorSauceToImage[ItalianSauce](Color.cadetBlue)
```

Try creating a `SuaceToImage` typeclass instance for `ItalianSauce` where each sauce has a different colour. What code changes must you make?

## Add-hoc polymorphism: pattern-matching warm up exercise

Read up on [pattern matching](https://docs.scala-lang.org/tour/pattern-matching.html). What do each of these symbols mean in a pattern match?
 - `@`
 - `:`
 - `_`

# Extension methods

Read up on [enriched interfaces](https://books.underscore.io/essential-scala/essential-scala.html#enriched-interfaces) in Scala 2. This introduces the ”ops” pattern, known as ”extension methods” in Scala 3. 

Can we use an extension method to clean up our `sauceImage` generation in `PizzaShop`?

```scala
val sauceImage: Image = Sauce.toImage[ItalianSauce](sauce, size)
```

# Recap: ad-hoc polymorphism

Both the Italian and French pizza shops should validate pizzas in the same way. We're next going to move the validation logic to the `core` package.

The `PizzaError` algebraic data type has been moved to `core`. A `Validation` object has been added for validation-related logic.

Take a look at the `Validation` object. Do you see any problems with moving the `validateSauce` to the `Validation` object? How you can address these with polymorphic code (would a typeclass help)?

# Algebraic data types and type parameters

The project fails to compile with the following error:

```
[error] -- [E007] Type Mismatch Error: pie/src/main/scala/pie/core/Validation.scala:25:87
[error] 25 |            case (Right(pizza), Right(sauce)) => Right(pizza.copy(size = size, sauce = sauce))
[error]    |                                                                                       ^^^^^
[error]    |                                        Found:    (sauce : T)
[error]    |                                        Required: pie.italy.ItalianSauce
[error] one error found
```

Take a look at the `validatePizza` function. What do you think is causing the error?

# Types and precision

Take a look at the following `validateSize` and `correction` functions.

```scala
def validateSize(size: Int): Either[PizzaError, Pizza] =
  if (size < 0) Left(NegativeSize)
  else if (size < 3) Left(PizzaTooSmall)
  else if (size > 16) Left(PizzaTooBig)
  else Right(Pizza(size, Core(Tomato2)))

def correction(error: PizzaError): Either[PizzaError, Pizza] = error match {
  case PizzaTooSmall => Right(Pizza(3, Core(Tomato2)))
  case PizzaTooBig => Right(Pizza(16, Core(Tomato2)))
  case other => Left(other)
}
```

 - Do you agree with the return type of these functions? Is there a better type than `Either[PizzaError, Pizza]`?
 - These functions use a default `ItalianSauce` value of `Core(Tomato2)`. How might you use a typeclass to write them for a generic sauce?

# Types and precision: counting types

The `Boolean` type represents two possible values: `true` or `false`.

1. How many values does the `PizzaError` type represent?
2. What about `Either[PizzaError, Unit]`?
3. What about the `Int` type?
3. What about `Pizza[T]`?

# Types and precision: validation

Our `ValidSize` datatype is defined as follows:

```scala
sealed trait ValidSize {
  val size: Int
}

object ValidSize {
    case object Three extends ValidSize {
        override val size: Int = 3
    }
    case object Four extends ValidSize  {
        override val size: Int = 4
    }
    case object Five extends ValidSize {
        override val size: Int = 5
    }
    case object Six extends ValidSize {
        override val size: Int = 6
    }

    val values: Set[ValidSize] = Set(Three, Four, Five, Six)
}
```

1. Use `values` to write a function that constructs an `Option[ValidSize]` from `Int`:

```scala
def makeValidSize(size: Int): Option[ValidSize] = ???
```

2. How can you use this in a function that constructs an `Either[PizzaError, ValidSize]`?

```scala
def validateSize(size: Int): Either[PizzaError, ValidSize] = ???
```


# Types and precision: typeclasses


Consider the following code in `ValidSize`

```
  implicit val ord: Ordering[ValidSize] = new Ordering[ValidSize] {
    override def  compare(x: ValidSize, y: ValidSize): Int = {
      ???
    }
  }
```

1. What is the typeclass that this relates to?
2. Which part of the code corresponds to the typeclass instance?
3. Implement `compare` using the `ValidSize.size` field.
4. Can you implement it using the `Ordering[Int]` instance?

# Types and precision: typeclass reuse

Take a look at the `ValidSize.scala` file:

1. The `minSize` is incorrectly set to `42`. Use the `min` function to find the correct `minSize`. 
2. Take a look at the `Ordering` instance for `ValidSize`.
  - How can you obtain an instance for `Ordering[Int]`?
  - Think of a way of reusing `Ordering[Int]` instance to construct the `Ordering[ValidSize]`.
3. cats has an `Order` typeclass that behaves similarly to `Ordering`.
  - Take a look at the `Order` typeclass. Can you construct an `Order` from an `Ordering`?
  - Can you use `Order` to simplify the implementation of `min`?

# Types and precision: typeclass reuse (continued)

Last week, we looked at implicits in depth, including the use of the `implicitly` function. We saw how we could simplify our `Ordering` instance for `ValidSize` by reusing the `Ordering` instance for `Int`. We then tried to simplify the implementation of `min`.

1. Look at `ValidSize.scala` file. Does a `NonEmptyList` have a `NonEmptyTraverse` instance? How do you know?
2. Look at the api docs for the [minimum function](https://typelevel.org/cats/api/cats/NonEmptyTraverse.html#minimum[A](fa:F[A])(implicitA:cats.Order[A]):A). We cannot call `minimum` on a `NonEmptyList[ValidSize]`. Why not?

# The ApplicativeError typeclass
 - Look at the correction function in `PizzaShop`. We can redefine this to use our new `ValidSize`. Write the implementation for this in the `Validation.scala` file:
 
 ```
  def correction(error: PizzaError): Either[PizzaError, ValidSize] = ???
 ```
 - 
 - Use the new `validateSize` and `correction` functions in the `validatePizza` function.


## Typeclass instances

Look at the `Validation.scala` file.
 - Are there any typeclasses defined in the file?
 - Does an `Either` have an `ApplicativeError` typeclass instance?
 - What about `Option`?
 - Can we define an `ApplicativeError` instance for `ValidSize`?

## Type shapes

We previously wrote the following code:

```scala
type TestType[T] = Either[String, T]
val eitherMonadError: MonadError[TestType, String] = MonadError.apply(implicitly)
```

 - How does this show that `Either` has a `MonadError` typeclass instance?
 - Does this show that `Either` has an `ApplicativeError` typeclass instance?
 - Why do we need to define `TestType`?
 - Instead of writing `MonadError.apply(implicitly)`, we could have written `MonadError[TestType, String]`. Why does this compile? 

## Type shapes and typeclasses

`ApplicativeError` has the following signature: `ApplicativeError[F[_], E]`. Which of these types fit the bounds:

 - `ApplicativeError[Either, ValidSize]`
 - `ApplicativeError[Option, PizzaError]`
   `ApplicativeError[ValidSize, PizzaError]`
 - `ApplicativeError[fs2.Stream, PizzaError]`
 - `ApplicativeError[IO, PizzaError]`

Do any of them correspond to valid typeclass instances?

## Using typeclass instances

Validating a size gives us an `eitherSizeOrError` value of type `Either[PizzaError, ValidSize]`:

```scala
val eitherSizeOrError: Either[PizzaError, ValidSize] = validateSize(size)
```

The current code does not use the `correction` function:

```
def correction(error: PizzaError): Either[PizzaError, ValidSize] = ???
```

1. Call the `correction` function by pattern matching on `eitherSizeOrError`.
2. Take a look at [`ApplicativeError`](https://typelevel.org/cats/api/cats/ApplicativeError.html). Are there any functions you could use instead?

# Using type parameters

Take a look at the `validateSize` function in `Validation.scala`:

```scala
def validateSize(size: Int): Either[PizzaError, ValidSize] = makeValidSize(size) match {
    case Some(validSize) => Right(validSize)
    case None =>
        if (size < 0) Left(NegativeSize)
        else if (size < ValidSize.minSize) Left(PizzaTooSmall)
        else Left(PizzaTooBig)
}
```

1. Use the `ApplicativeError` instance for `MyEither` to replace the calls to `Right` and `Left` with functions provided by `ApplicativeError`.
   As an example, `Left` can be replaced with `raiseError`.
2. Can you add a type parameter and typeclass argument so that this function can be called using any effect?

## Type parameters and typeclasses

1. Parameterize the `correction` function by `F[_]`. 

```scala
def correction[F[_]](error: PizzaError)(???): F[ValidSize] = ???

```

2. Can you write the following function to "lift" an `Either` into any `F[_]` that has an `ApplicativeError` instance?

```scala
def fromEither[F[_], E, A](either: Either[E, A])(???): F[A] = either match {
???
}
```

4. Can you parameterize `validatePizza` by `F[_]` using the same pattern? If not, why?


## Type parameters and typeclasses (continued)


Take a look at the `Validation.scala` file:

1. Using `validateSize` and `correction` as examples, can you write the following function to "lift" an `Either` into any `F[_]` that has an `ApplicativeError` instance?

```scala
def fromEither[F[_], E, A](either: Either[E, A])(???): F[A] = either match {
???
}
```

2. Use this in `validatePizza` to lift the `eitherSauceOrError` into an effect.

3. Can you parameterize `validatePizza` by `F[_]` using the same pattern?

## Type classes with type parameters

Take a look at the `SauceParser`. It's apply function returns an `Either[StrangeSauce.type, T]`.

1. Can you refactor the `SauceParser` to return an `F[T]` instead, where `F[_]` has an `ApplicativeError[F, PizzaError]` by:
  a. Parameterizing `SauceParser` by `F[_]`.
  b. Parameterizing `apply` by `F[_]`.
  What is the difference between either case?

## Type parameters, functions and traits

Let's create a `Default` typeclass that provides a default value.

Consider the following signatures of `Default`:

```scala
trait Default[A] {
  val default: A
}

trait Default {
  def default[A]: A
}
```

Which signature is appropriate? Can you implement them both?

### Type parameters and quantification

Write a `getOrDefault` function that takes an `Option[A]` and returns a default value of `A` in the case of `None`. 

How does the following trait behave?

```scala
trait Default[A] {
  def default[A]: A
}
```

### Universal quantification

1. Implement the following functions in as many ways as you can:
```scala
def default[A]: A = ???
def mao[A](a: A): A = ???
def maru[A](a1: A, a2: A): A = ???
```

2. How does `def default[A]: A = ???` differ to the `trait Default[A]`?
