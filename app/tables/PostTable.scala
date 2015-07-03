package tables

import slick.driver.JdbcProfile

trait PostTable {
  protected val driver: JdbcProfile
  import driver.api._

  class Posts(tag: Tag) extends Table[(Int, String)](tag, "posts") {

    def id = column[Int]("id", O.PrimaryKey)
    def title = column[String]("title")

    def * = (id, title)
  }
}
