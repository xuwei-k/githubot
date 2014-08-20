package githubot

case class UserAction(
  id: UserActionID,
  url: String,
  title: String,
  published: String,
  imageUrl: Option[String]
) {

  def tweetString: String = {
    (url + " " + title + " " + published).take(140)
  }

  def imageStream(size: Int): Option[java.io.InputStream] =
    imageUrl.flatMap{ imgUrl =>
      try {
        val u = new java.net.URL(imgUrl + "?s=" + size)
        Option(u.openConnection.getInputStream)
      } catch {
        case e: Throwable =>
          println(e)
          None
      }
    }
}

object UserAction{
  def apply(rawData: xml.Node): UserAction = {
    UserAction(
      (rawData \ "id").text.trim,
      (rawData \ "link" \ "@href").text.trim,
      (rawData \ "title").text.trim,
      (rawData \ "published").text.trim,
      (rawData \ "thumbnail" \ "@url").text.trim.split('?').headOption.filter(_.startsWith("http"))
    )
  }
}
