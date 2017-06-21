package controllers

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api._
import play.api.mvc._
import play.api.i18n.Messages.Implicits._
import play.api.i18n.{MessagesApi, I18nSupport}
import domain.entity.User
import domain.repository.UsersRepository

@Singleton
class SessionController @Inject() (
  val messagesApi: MessagesApi,
  val usersRepository: UsersRepository
) extends Controller
  with Secured
  with I18nSupport
{

  val form = Form(
    mapping(
      "screen_name" -> text,
      "password" -> text
    )(User.LoginForm.apply)(User.LoginForm.unapply)
  )

  def login = withUser { currentUser => implicit request =>
    Ok(views.html.session.login(form, currentUser))
  }

  def create = withUser { currentUser => implicit request =>
    currentUser.map { _ =>
      Redirect(routes.SessionController.logout())
    }.getOrElse {
      form.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.session.login(formWithErrors, None))
        },
        data => {
          usersRepository.findByScreenName(data.screen_name)
            .filter(u => usersRepository.authenticate(u, data.password))
            .map(user =>
              loginWith(
                user,
                Redirect(routes.UsersController.show(user.id.value))
              )
            )
            .getOrElse(BadRequest(views.html.session.login(form, None)))
        }
      )
    }
  }

  def logout = withUser { currentUser => implicit request =>
    currentUser.map { _ =>
      Ok(views.html.session.logout(currentUser))
    }.getOrElse {
      Redirect(routes.HomeController.index())
    }
  }

  def destroy = Action { implicit request =>
    logout(Ok(views.html.index(None)))
  }
}
