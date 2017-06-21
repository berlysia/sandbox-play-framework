package infra.repository
import domain.repository.UsersRepository
import domain.entity.User
import domain.valueObject.UserId

import com.google.inject.{Inject, Singleton}
import com.roundeights.hasher.Implicits._
import scala.language.postfixOps
import scala.collection.mutable.{Map, HashMap}

@Singleton
class UsersRepositoryOnMemoryImpl @Inject() extends UsersRepository {
  val byId: Map[UserId, User] = HashMap[UserId, User]()
  val byScreenName: Map[String, User] = HashMap[String, User]()

  def authenticate(user: User, password: String): Boolean =
    User.digest(password) == user.passwordDigest

  def findById(id: UserId): Option[User] = byId.get(id)
  def findByScreenName(screenName: String): Option[User] = byScreenName.get(screenName)

  def save(user: User): Option[User] = {
    val updatedUser = if (user.id.isZero) {
      val id = UserId.create
      user.copy(id = id)      
    } else user

    byId.update(updatedUser.id, updatedUser)
    byScreenName.update(updatedUser.screenName, updatedUser)

    Some(updatedUser)
  }
}
