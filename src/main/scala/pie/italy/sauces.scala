package pie.italy

import doodle.core.Color
import doodle.image.Image
import pie.core.{CoreSauce, FixedColorSauceToImage, SauceToImage}


sealed trait ItalianSauce

object ItalianSauce{
  implicit val sauceToImage: FixedColorSauceToImage[ItalianSauce] =  new FixedColorSauceToImage[ItalianSauce](Color.cadetBlue)
}

object Napoli extends ItalianSauce {
  // override def sauceColor = Color.orange
  // override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke

  implicit val sauceToImage: FixedColorSauceToImage[Napoli.type] = new FixedColorSauceToImage[Napoli.type](Color.orange)
}

object Bologna extends ItalianSauce {
  // override def sauceColor: Color = Color.brown
  // override def toImage(size: Int): Image =  Image.circle(size * 0.75).fillColor(sauceColor).noStroke

  implicit val sauceToImage: FixedColorSauceToImage[Bologna.type] = new FixedColorSauceToImage[Bologna.type](Color.brown)
}
final case class Core(core: CoreSauce) extends ItalianSauce {
//  def toImage[A](size: Int)(implicit sauceToImage: SauceToImage[A]): Image = sauceToImage.toImage(size)

}

object Mao {
   val sauceToImage: FixedColorSauceToImage[Core] = ??? // new FixedColorSauceToImage[Bologna.type](Color.brown)
}


//sealed trait Animal
//// all cats are animals
//case class Cat(name: String) extends Animal
//// all fish are animals
//case class Fish(name: String) extends Animal
//
//// Typeclasses really should be invariant
//class IsFurry[A]
//
//object IsFurry {
//  def useFurriness[A](a: A)(implicit isFurry: IsFurry[A]): Unit  = ???
//
//  // IsFurry[Cat] is completely unrelated to IsFurry[Animal]
//  // all cats are animals, but this does not mean that all animals are furry becase all cats are furry
//  // This is known as INVARIANT
//  // all cats are animal, but not all animals are cats
//  val fish: Animal = Fish("Nemo")
//  val mao: Animal = Cat("mao")
//
//  // all cats are furry
//  implicit val isFurry: IsFurry[Cat] = new IsFurry[Cat]
//
//  useFurriness[Animal](fish)
//}

