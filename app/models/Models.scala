package models
import java.sql.Timestamp
import com.github.rjeschke.txtmark

case class Post(
  id: Long,
  title: String,
  slug: String,
  content: String,
  publishDate: Timestamp
) {

  lazy val html = txtmark.Processor.process(content)
}
