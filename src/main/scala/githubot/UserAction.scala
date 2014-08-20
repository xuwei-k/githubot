package githubot

case class UserAction(
  id: UserActionID,
  url: String,
  title: String,
  published: String
) {

  def tweetString: String = {
    (url + " " + title + " " + published).take(140)
  }
}

object UserAction{
  def apply(rawData: xml.Node): UserAction = {
    UserAction(
      (rawData \ "id").text.trim,
      (rawData \ "link" \ "@href").text.trim,
      (rawData \ "title").text.trim,
      (rawData \ "published").text.trim
    )
  }
}
