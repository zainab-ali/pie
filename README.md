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

These exercises are tested by the `HandfulOfSlicesTest`.  You can run it with

```
sbt testOnly pie.HandfulOfSlicesTest
```

An olive can be sliced into slices. Each slice has all the properties of its olive and is defined as:

```scala
case class OliveSlice(olive: Olive)
```

1. Write a function `sliceOlive` that slices an olive into a handful of three pieces.

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
