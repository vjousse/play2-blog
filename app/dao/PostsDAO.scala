package dao

import scala.concurrent.Future

import models.Post
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import java.sql.Timestamp
import collection.immutable.SortedMap
import org.joda.time.DateTime

trait PostsComponent { self: HasDatabaseConfig[JdbcProfile] =>
  import driver.api._

  class Posts(tag: Tag) extends Table[Post](tag, "blog_blogpost") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("title")
    def slug = column[String]("slug")
    def content = column[String]("content")
    def publishDate = column[Timestamp]("publish_date")
    def * = (id, name, slug, content, publishDate) <> (Post.tupled, Post.unapply _)
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

  /** Retrieve a post from the slug. */
  def findBySlug(slug: String): Future[Option[Post]] =
    db.run(posts.filter(_.slug === slug).result.headOption)

  /** Count all posts. */
  def count(): Future[Int] =
    db.run(posts.length.result)

  /** Get all posts */
  def findAll(): Future[Seq[Post]] =
    db.run(posts.sortBy(_.publishDate.desc).result)

  /** Get all posts */
  def findAllGroupedByMonth(): Future[Map[DateTime, Seq[Post]]] = {

    implicit def dateTimeOrdering: Ordering[DateTime] = Ordering.fromLessThan(_ isBefore _)

    db.run(posts.sortBy(_.publishDate.desc).result).map { r =>
      SortedMap(r.groupBy(p => {
        new DateTime(p.year, p.month, 1, 0, 0)
      }).toSeq:_*)(Ordering[DateTime].reverse)
    }
  }

}
