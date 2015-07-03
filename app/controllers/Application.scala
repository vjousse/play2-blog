package controllers

import play.api._
import play.api.mvc._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

class Application extends Controller {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig.driver.api._

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /*
  def index = Action.async { implicit request =>
    val resultingUsers: Future[Seq[User]] = dbConfig.db.run(Users.filter(_.name === name).result)
    resultingUsers.map(users => Ok(views.html.index(users)))
  }
  */

}
