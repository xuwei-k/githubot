package githubot

import scalaj.http.Http
import scalaj.http.HttpOptions._

object Mail{
  case class Conf(to: String, password: String)

  private val defaultOptions = List(
    allowUnsafeSSL, connTimeout(30000), readTimeout(30000)
  )

  private val gaemailUrl = "http://gae-mail.appspot.com/"

  def apply(subject: String, message: String, conf: Conf): String = {
    import argonaut.Json
    val jsonString = Json.obj(
      "to" -> Json.jString(conf.to),
      "subject" -> Json.jString(subject),
      "message" -> Json.jString(message),
      "password" -> Json.jString(conf.password),
      "attachments" -> Json.obj()
    ).toString
    Http.postData(gaemailUrl, jsonString).options(defaultOptions).asString
  }

}

