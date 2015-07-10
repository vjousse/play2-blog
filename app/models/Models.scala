package models
import java.sql.Timestamp
import com.github.rjeschke.txtmark
import org.joda.time.DateTime

case class Post(
  id: Long,
  title: String,
  slug: String,
  content: String,
  publishDate: Timestamp
) {

  lazy val html = txtmark.Processor.process(content)

  lazy val month: Int = new DateTime(publishDate).getMonthOfYear()

  lazy val year: Int = new DateTime(publishDate).getYear()
}

case class Category(
  id: Long,
  title: String,
  slug: String
)

case class PostCategory(
  id: Long,
  postId: Long,
  categoryId: Long
)
