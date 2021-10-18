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
