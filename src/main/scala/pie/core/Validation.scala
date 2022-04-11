package pie.core

object Validation {
    def parseSize(size: String): Either[NotASize.type, Int] =
    size.toIntOption.toRight(NotASize).map(_ * 10)
}
