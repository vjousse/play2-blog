package models
import java.sql.Timestamp
import com.github.rjeschke.txtmark
import java.util.{ Calendar, GregorianCalendar }

case class Post(
  id: Long,
  title: String,
  slug: String,
  content: String,
  publishDate: Timestamp
) {

  lazy val html = txtmark.Processor.process(content)

  lazy val month: Int = {
    var cal = new GregorianCalendar()
    cal.setTime(publishDate)
    cal.get(Calendar.MONTH)
  }


  lazy val year: Int = {
    var cal = new GregorianCalendar()
    cal.setTime(publishDate)
    cal.get(Calendar.YEAR)
  }
}
