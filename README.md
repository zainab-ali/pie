# Pie

This is a tutorial on functional programming in Scala with cats, cats-effect and fs2.

## Getting Started

1. Type `sbt` to enter the sbt console.
2. Run the application with `run 12`.
   This should draw a 12 inch pizza with a single olive (you'll need to use your imagination here).
   
Take a look inside the `pie.PizzaShop` to see what gets run.
   
## Algebraic Data Types

Create an algebraic data type `Pizza` with a `size` and a `sauce`.
 - The size should be a number.
 - The sauce should be `Bechamel` or `Tomato`
 
## Basic validation with the Either datatype

Take a look at the tests in `ValidateSizeTest`. Write a function `validateSize` that:
 - takes a pizza size as an argument
 - evaluates to either a pizza or an error

The error must be one of:
 - `NegativeSize`
 - `PizzaTooBig`
 - `PizzaTooSmall`
 
## Error handling with the Either datatype

Write a function `correction`. As an argument, it should take in an error.
 - takes an error as an argument
 - If the error is a `PizzaTooBig` error, it evaluates to a 16 inch pizza
 - If the error is a `PizzaTooSmall` error, it evaluates to a 3 inch pizza
 - If the error is none of the above, it evaluates to that same error

## Validation with the cats Validated datatype

Write a function `validateSauce` that:
 - takes a sauce name as an argument
 - evaluates to either `Bechamel`, `Tomato` or a `StrangeSauce` error

Use a `Validated` to evaluate both `vaildateSize` and `validateSauce`.
