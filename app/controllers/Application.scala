package controllers

import play.api._
import play.api.mvc._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.db.slick.HasDatabaseConfig
import tables.PostTable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Application extends Controller with PostTable with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig.driver.api._

  def index = Action.async { implicit request =>
    val posts = TableQuery[Posts]
    val resultingPosts: Future[Seq[(Int, String)]] = dbConfig.db.run(posts.result)
    resultingPosts.map(posts => Ok(views.html.index(posts)))
  }

}
