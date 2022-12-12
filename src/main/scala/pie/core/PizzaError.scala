package pie.core

sealed trait PizzaError extends Throwable
case object NonIntSize extends PizzaError
case object NegativeSize extends PizzaError
case object PizzaTooBig extends PizzaError
case object PizzaTooSmall extends PizzaError
case object StrangeSauce extends PizzaError
