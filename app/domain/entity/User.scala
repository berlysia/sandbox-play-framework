package domain.entity
import java.time.LocalDateTime
import domain.valueObject.UserId

import com.roundeights.hasher.Implicits._
import scala.language.postfixOps

object User {
  case class LoginForm(
    screen_name: String,
    password: String
  )

  case class SignupForm(
    screen_name: String,
    display_name: String,
    email: String,
    password: String,
    password_confirm: String
  )

  def digest(password: String): String = password.bcrypt.hex
  def create(sf: SignupForm): Option[User] = {
    if(sf.password == sf.password_confirm)
      Some(User(UserId.zero, sf.screen_name, sf.display_name, sf.email, LocalDateTime.now, digest(sf.password)))
    else
      None
  }
}
case class User(
  id: UserId,
  screenName: String,
  displayName: String,
  email: String,
  createdAt: LocalDateTime,
  passwordDigest: String
) {
  def follow(o: User): Boolean = ???
  def unfollow(o: User): Boolean = ???
  def tweet(content: String): Boolean = ???
}
