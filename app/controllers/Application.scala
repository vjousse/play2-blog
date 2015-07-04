package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import dao.PostsDAO
import models.Post

class Application extends Controller {

  def postsDao = new PostsDAO

  def index = Action.async { implicit request =>
    val resultingPosts: Future[Seq[Post]] = postsDao.findAll
    resultingPosts.map(posts => Ok(views.html.index(posts)))
  }

}
