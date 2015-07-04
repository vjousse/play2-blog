package dao

import scala.concurrent.Future

import models.Post
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

trait PostsComponent { self: HasDatabaseConfig[JdbcProfile] =>
  import driver.api._

  class Posts(tag: Tag) extends Table[Post](tag, "blog_blogpost") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("title")
    def * = (id, name) <> (Post.tupled, Post.unapply _)
  }
}


class PostsDAO extends PostsComponent with HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig =  DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  val posts = TableQuery[Posts]

  /** Insert a new post */
  def insert(post: Post): Future[Unit] =
    db.run(posts += post).map(_ => ())

  /** Insert new posts */
  def insert(posts: Seq[Post]): Future[Unit] =
    db.run(this.posts ++= posts).map(_ => ())

  /** Retrieve a post from the id. */
  def findById(id: Long): Future[Option[Post]] =
    db.run(posts.filter(_.id === id).result.headOption)

  /** Count all posts. */
  def count(): Future[Int] =
    db.run(posts.length.result)

  /** Get all posts */
  def findAll(): Future[Seq[Post]] = db.run(posts.result)

}
