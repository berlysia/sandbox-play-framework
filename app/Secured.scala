package controllers

import com.google.inject.{Inject, Singleton}
import play.api.mvc._
import domain.entity.User
import domain.valueObject.UserId
import domain.repository.UsersRepository

trait Secured  {
  val usersRepository: UsersRepository
  private val SESSION_USER_ID_KEY = Security.username

  private def userId(request: RequestHeader): Option[UserId] =
    request.session.get(SESSION_USER_ID_KEY)
      .map(s => UserId(s.toLong))

  private def onUnauthorized(request: RequestHeader) =
    Results.Redirect(routes.SessionController.login).withNewSession

  def withAuth (process: => UserId => Request[AnyContent] => Result) = {
    Security.Authenticated(userId, onUnauthorized) { id =>
      Action(request => process(id)(request))
    }
  }

  def withUser (process: Option[User] => Request[AnyContent] => Result) = Action { implicit request =>
    process(usersRepository.findById(userId(request).getOrElse(UserId.zero)))(request)
  }

  def loginWith (user: User, result: Result)(implicit request: Request[_]): Result =
    result.withSession(request.session + (SESSION_USER_ID_KEY -> user.id.value.toString))

  def logout (result: Result) = result.withNewSession
}

