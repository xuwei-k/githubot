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
    UserAction.resizeTweetString(u + title + "\n\n" + formattedContent)
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
    }.toList.distinct

    lines.mkString("\n")
  }
}

object UserAction {
  private[this] val escapeMap = Map(
    "@" -> "🍥",
    "\\.md" -> "_md",
    "\\.sh" -> "_sh",
    "\\.py" -> "_py",
    "\\.java" -> "_java",
    "\\.Final" -> "_Final",
    "of github.com:" -> "of github_com:"
  )

  def escape(str: String): String =
    escapeMap.foldLeft(str) { case (s, (k, v)) => s.replaceAll(k, v) }

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

  def isASCII(c: Char): Boolean = {
    (0x0 <= c) && (c <= 0x7f)
  }

  def resizeTweetString(tweet: String): String = {
    if (tweet.length <= 140) {
      tweet
    } else {
      val original = tweet.toCharArray
      val buf = new java.lang.StringBuilder()
      @annotation.tailrec
      def loop(i: Int, size: Int): Unit = {
        original.lift.apply(i) match {
          case Some(char) =>
            val nextSize = size + {
              if (isASCII(original(i))) {
                1
              } else {
                2
              }
            }
            if (nextSize > 280) {
              ()
            } else {
              buf.append(char)
              loop(i + 1, nextSize)
            }
          case None =>
            ()
        }
      }
      loop(0, 0)
      buf.toString
    }
  }
}
