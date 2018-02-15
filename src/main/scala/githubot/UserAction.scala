package githubot

import java.io.ByteArrayInputStream
import java.io.InputStream
import org.apache.commons.text.StringEscapeUtils
import scala.annotation.tailrec

final case class UserAction(
  id: UserActionID,
  url: String,
  title: String,
  published: String,
  content: String
) {

  def tweetString: String = {
    val u = if (url == "https://github.com/" || title.contains(" deleted branch ")) {
      ""
    } else {
      url + " "
    }
    u + title + "\n\n" + formattedContent
  }

  private[this] def formattedContent: String = {
    @tailrec
    def loop(str: String): String = {
      val replaced = UserAction.trimMap.foldLeft(str) {
        case (s, (oldStr, newStr)) =>
          s.replaceAll(oldStr, newStr)
      }
      if (replaced != str) {
        loop(replaced)
      } else {
        replaced
      }
    }
    val gitHash = (('0' to '9') ++ ('a' to 'f')).toSet
    val result = loop(UserAction.escape(StringEscapeUtils.unescapeXml(content).replaceAll("\\<[^>]*>", "")))
    val lines = result.lines.filterNot { s =>
      title.contains(s) || s.contains(title) || s.forall(gitHash)
    }.toList

    {
      if (lines.nonEmpty) {
        lines.init
      } else {
        lines
      }
    }.mkString("\n")
  }
}

object UserAction {
  private[this] val escapeMap = Map(
    "@" -> "🍥",
    "\\.md" -> "_md",
    "\\.java" -> "_java",
    "of github.com:" -> "of github_com:"
  )

  def escape(str: String): String =
    escapeMap.foldLeft(str) { case (s, (k, v)) => s.replace(k, v) }

  private val trimMap: Map[String, String] = Map(
    ("\n\n\n", "\n\n"),
    ("  ", " "),
    (" \n", "\n"),
    ("\n ", "\n")
  )

  def getImageStream(tweetHtml: String): InputStream = {
    val bytes = IO.html2byteArray(tweetHtml)
    new ByteArrayInputStream(bytes)
  }

  def apply(rawData: xml.Node): UserAction = {
    UserAction(
      (rawData \ "id").text.trim,
      (rawData \ "link" \ "@href").text.trim,
      (rawData \ "title").text.trim,
      (rawData \ "published").text.trim,
      (rawData \ "content").text.trim
    )
  }
}
