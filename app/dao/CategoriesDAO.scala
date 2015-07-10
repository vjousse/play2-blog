package dao

import scala.concurrent.Future

import models.{ Category, PostCategory }
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import java.sql.Timestamp

trait CategoriesComponent { self: HasDatabaseConfig[JdbcProfile] =>
  import driver.api._

  class Categories(tag: Tag) extends Table[Category](tag, "blog_blogcategory") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("title")
    def slug = column[String]("slug")
    def * = (id, name, slug) <> (Category.tupled, Category.unapply _)
  }


  class PostCategories(tag: Tag) extends Table[PostCategory](tag, "blog_blogpost_categories") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def postId = column[Long]("blogpost_id")
    def categoryId = column[Long]("blogcategory_id")

    def * = (id, postId, categoryId) <> (PostCategory.tupled, PostCategory.unapply _)

  }
}

class CategoriesDAO extends CategoriesComponent with HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig =  DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  val categories = TableQuery[Categories]

  /** Retrieve a category from the id. */
  def findById(id: Long): Future[Option[Category]] =
    db.run(categories.filter(_.id === id).result.headOption)

  /** Retrieve a category from the slug. */
  def findBySlug(slug: String): Future[Option[Category]] =
    db.run(categories.filter(_.slug === slug).result.headOption)

  /** Count all categories. */
  def count(): Future[Int] =
    db.run(categories.length.result)


}
