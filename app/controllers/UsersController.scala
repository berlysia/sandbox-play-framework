package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.i18n.{MessagesApi, I18nSupport}
import domain.entity.User
import domain.valueObject.UserId
import domain.repository.UsersRepository

@Singleton
class UsersController @Inject() (
  val messagesApi: MessagesApi,
  val usersRepository: UsersRepository
) extends Controller
  with Secured
  with I18nSupport
{
  val form = Form(
    mapping(
      "screen_name" -> text,
      "display_name" -> text,
      "email" -> text,
      "password" -> text,
      "password_confirm" -> text
    )(User.SignupForm.apply)(User.SignupForm.unapply)
  )

  def show(id: Long) = withUser { currentUser => implicit request =>
    usersRepository
      .findById(UserId(id))
      .map(u => Ok(views.html.users.show(u, currentUser)))
      .getOrElse(NotFound)
  }

  def signup = withUser { currentUser => implicit request =>
    currentUser.map(_ =>
      Redirect(routes.SessionController.logout())
    ).getOrElse(
      Ok(views.html.users.signup(form, currentUser))
    )
  }

  def create = withUser { currentUser => implicit request =>
    currentUser.map { _ =>
      Redirect(routes.SessionController.logout())
    }.getOrElse {
      form.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.users.signup(formWithErrors, None))
        },
        data => {
          val result = for (
            newUser <- User.create(data);
            savedUser <- usersRepository.save(newUser)
          ) yield {
            loginWith(
              savedUser,
              Redirect(routes.UsersController.show(savedUser.id.value))
            )
          }
          result.getOrElse(BadRequest(views.html.users.signup(form, None)))
        }
      )
    }
  }
}
