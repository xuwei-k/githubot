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

}

object UserAction{

  def getImageStream(action: UserAction): InputStream = {
    val bytes = IO.html2byteArray(action.content)
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
