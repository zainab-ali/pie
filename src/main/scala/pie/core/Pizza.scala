package pie.core

final case class Pizza[T](size: Int, sauce: T)

// Singleton Companian Object
object Pizza {
  def apply[T](size: Int, sauce: T): Pizza[T] = new Pizza[T](size, sauce) // Val of Pizza returned as Object
}