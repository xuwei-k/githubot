package githubot

import java.io.ByteArrayInputStream
import java.io.InputStream

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
    (u + title + " " + published)
  }

}

object UserAction {

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
