package models
import java.sql.Timestamp

case class Post(
  id: Long,
  title: String,
  slug: String,
  content: String,
  publishDate: Timestamp
)
