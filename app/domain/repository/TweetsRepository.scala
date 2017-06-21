package domain.repository
import domain.entity.Tweet
import domain.valueObject.TweetId

trait TweetsRepository {
    def findById(id: TweetId): Option[Tweet]
    def save(tweet: Tweet): Option[Tweet]
    def destroy(tweet: Tweet): Boolean
}
