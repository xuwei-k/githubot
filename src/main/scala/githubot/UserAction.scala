package githubot

import java.io.{ByteArrayInputStream, InputStream}

final case class UserAction(
  id: UserActionID,
  url: String,
  title: String,
  published: String,
  content: String
) {

  def tweetString: String = {
    (url + " " + title + " " + published).take(140)
  }

  def image: InputStream = {
    val bytes = IO.html2byteArray(id, content)
    new ByteArrayInputStream(bytes)
  }
}

object UserAction{

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
