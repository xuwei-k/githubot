import java.net.URL
import githubot._
import scala.concurrent.duration._

new Config{
  val rss = new URL("")
  override val firstTweet = false
  val interval = 3.minute
  override val filter = { (u: UserAction) =>
    val s = u.tweetString
    List("codecov-io", "googlebot", "asfgit", "grpc-jenkins", "netkins", "buildhive", "scala-jenkins", "akka-ci", "asfbot", "ConfluentJenkins").forall(disable => ! s.contains(disable))
  }
  val twitter = new TwitterSettings{
    val consumerKey       = ""
    val consumerSecret    = ""
    val accessToken       = ""
    val accessTokenSecret = ""
  }

  private[this] val shouldNotAddImageWords = Set(
    "starred", "forked", "created branch", "created repository", "deleted branch", "created tag"
  ).map(" " + _ + " ")

  override val addImage = { (action: UserAction) =>
    ! shouldNotAddImageWords.exists(word => action.title.contains(word))
  }

  private def removeHtmlTags(string: String): Option[String] =
    try {
      Option(scala.xml.XML.loadString(string).text)
    } catch {
      case _: Exception =>
        None
    }

  private def splitLine(str: String): String = {
    val delimiters = List(' ', '。', '、', '　', ' ')
    val words: Iterator[String] = delimiters.foldLeft(Array(str.trim)){ 
      (strings, c) => strings.flatMap(_.split(c))
    }.iterator
    var lines: List[String] = Nil
    var current = new StringBuilder(" ")
    while (words.hasNext) {
      if (current.size > 80) {
        lines ::= current.result()
        current = new StringBuilder(words.next() + " ")
      } else {
        current.append(words.next())
        current.append(" ")
      }
    }
    lines ::= current.result()
    lines.reverseIterator.mkString("<br />")
  }

  private[this] final val titleClassDiv = """<div class="title">"""
  private[this] final val timeClassDiv = """<div class="time">"""

  def action2html(action: UserAction): String = {
    val getLine = action.content.lines.toArray.lift

    def isIgnoreDivElem(line: String, index: Int, div: String) = {
      (line == div) ||
      (getLine(index - 1).exists(_ == div)) ||
      (getLine(index - 2).exists(_ == div))
    }

    action.content.lines.zipWithIndex.filterNot{
      case (line, index) =>
        isIgnoreDivElem(line, index, titleClassDiv) || isIgnoreDivElem(line, index, timeClassDiv)
    }.map{
      case (line, index)
        if line.trim.size > 80 &&
           (getLine(index - 1).exists(_.contains("<blockquote>")) || getLine(index + 1).exists(_.contains("</blockquote>")))
         =>

        removeHtmlTags(line) match {
          case Some(str) =>
            splitLine(str)
          case None =>
            if(line.contains("<a href=") || line.contains("<img ")){
              line
            } else {
              splitLine(line)
            }
        }
      case (line, _) =>
        line
    }.map(_.replace("<p>", """<p style="text-align: center;">""")).mkString("\n")
  }

}
