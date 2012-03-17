package githubot

import scalaj.http.Http
import scalaj.http.HttpOptions._

object Mail{
  case class Conf(to:String,password:String)

  val underlying =
    Http.post("http://gae-mail.appspot.com/").options(
      allowUnsafeSSL,connTimeout(30000),readTimeout(30000)
    )

  def apply(subject:String,message:String,conf:Conf) = {
    underlying.params(
      "to" -> conf.to,"subject" -> subject,"message" -> message,
      "password"-> conf.password
    ).asString
  }

}

