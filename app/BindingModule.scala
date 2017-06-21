package modules

import com.google.inject.AbstractModule
import play.api._
import domain.repository._
import infra.repository._

class BindingModule extends AbstractModule {
  def configure() = {
    bind(classOf[UsersRepository]).to(classOf[UsersRepositoryOnMemoryImpl])
  }
}