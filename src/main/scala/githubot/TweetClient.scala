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

  def tweet(a: UserAction, image: Option[InputStream], charCount: Int): Unit = {
    allCatchPrintStackTrace{
      try{
        val tweet = a.tweetString.take(charCount)
        image match {
          case Some(stream) =>
            val status = new StatusUpdate(tweet).media("img", stream)
            t.updateStatus(status)
          case None =>
            t.updateStatus(tweet)
        }
      }catch{
        case e: Throwable =>
          println(e)
          t.updateStatus(a.tweetString.take(140))
      }
    }
  }

}


