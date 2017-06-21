package domain.valueObject

object UserId {
  private val zeroValue = 0
  private[this] var counter: Long = 0
  private[this] def increment: Long = {
    counter += 1
    counter
  }
  def create = UserId(increment)
  def zero = UserId(zeroValue)
}
case class UserId(id: Long) extends AnyVal {
  def isZero = id == UserId.zeroValue
  def value = id
}
