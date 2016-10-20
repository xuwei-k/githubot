package githubot

case class UserAction(
  id: UserActionID,
  url: String,
  title: String,
  published: String
) {

  def tweetString: String = {
    val u = if(url == "https://github.com/" || title.contains(" deleted branch ")) {
      ""
    } else {
      url + " "
    }
    (u + title + " " + published).take(140)
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
