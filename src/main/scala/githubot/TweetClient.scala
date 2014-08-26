package githubot

import java.io.InputStream
import twitter4j._
import twitter4j.conf._

final case class TweetClient(conf: TwitterSettings) {

  val t = {
    val c = new ConfigurationBuilder
      c.setDebugEnabled(true)
      .setOAuthConsumerKey(conf.consumerKey)
      .setOAuthConsumerSecret(conf.consumerSecret)
      .setOAuthAccessToken(conf.accessToken)
      .setOAuthAccessTokenSecret(conf.accessTokenSecret);

    new TwitterFactory(c.build()).getInstance()
  }

  def tweet(a: UserAction, image: Option[InputStream]): Unit = {
    allCatchPrintStackTrace{
      try{
        image match {
          case Some(stream) =>
            val status = new StatusUpdate(a.tweetString).media("img", stream)
            t.updateStatus(status)
          case None =>
            t.updateStatus(a.tweetString)
        }
      }catch{
        case e: Throwable =>
          println(e)
          t.updateStatus(a.tweetString)
      }
    }
  }

}


