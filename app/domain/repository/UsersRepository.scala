package domain.repository
import domain.entity.User
import domain.valueObject.UserId

trait UsersRepository {
    def authenticate(u: User, password: String): Boolean
    def findById(id: UserId): Option[User]
    def findByScreenName(screenName: String): Option[User]
    def save(user: User): Option[User]
}
