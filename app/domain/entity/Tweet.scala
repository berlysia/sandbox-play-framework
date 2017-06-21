package domain.entity
import java.time.LocalDateTime
import domain.valueObject.{TweetId, UserId}

object Tweet {
}

case class Tweet(
    id: TweetId,
    userId: UserId,
    content: String,
    createdAt: LocalDateTime
)
