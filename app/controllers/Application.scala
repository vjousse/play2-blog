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
    postsDao.findAllGroupedByMonth.map(posts => Ok(views.html.index(posts)))
  }

  def post(slug: String) = Action.async { implicit request =>
    val test = postsDao.findBySlugJoined(slug)
    postsDao.findBySlug(slug).map(_ match {
      case Some(post) => Ok(views.html.post(post))
      case None => NotFound
    })
  }


}
