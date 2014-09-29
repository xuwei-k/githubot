import java.net.URL
import githubot._
import scala.concurrent.duration._

new Config{
  val rss = new URL("")
  val interval = 5.minute
  val filter: UserAction => Boolean = Function.const(true)

  val twitter = new TwitterSettings{
    val consumerKey       = ""
    val consumerSecret    = ""
    val accessToken       = ""
    val accessTokenSecret = ""
  }

  def action2html(action: UserAction): String = {
    val getLine = action.content.lines.toArray.lift
    action.content.lines.zipWithIndex.map{
      case (line, index)
        if line.size > 50 &&
           getLine(index - 1).exists(_.contains("<blockquote>")) &&
           getLine(index + 1).exists(_.contains("</blockquote>")) &&
           (false == line.contains("<a href=")) &&
           (false == line.contains("<img "))
         =>

        val words = line.split(' ').iterator
        var lines: List[String] = Nil
        var current = new StringBuilder(" ")
        while (words.hasNext) {
          if (current.size > 40) {
            lines ::= current.result()
            current = new StringBuilder(words.next() + " ")
          } else {
            current.append(words.next())
            current.append(" ")
          }
        }
        lines ::= current.result()
        lines.reverseIterator.mkString("<br />")
      case (line, _) =>
        line
    }.mkString("\n")
  }

}

