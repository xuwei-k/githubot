package githubot

import java.io.InputStream
import twitter4j.Twitter
import twitter4j.v1.StatusUpdate

final case class TweetClient(conf: TwitterSettings) {

  val t = {
    Twitter
      .newBuilder()
      .prettyDebugEnabled(true)
      .oAuthConsumer(conf.consumerKey, conf.consumerSecret)
      .oAuthAccessToken(conf.accessToken, conf.accessTokenSecret)
      .build()
      .v1()
      .tweets()
  }

  def tweet(a: UserAction, image: Option[InputStream]): Unit = {
    allCatchPrintStackTrace {
      try {
        val tweet = a.tweetString
        image match {
          case Some(stream) =>
            val status = StatusUpdate.of(tweet).media("img", stream)
            t.updateStatus(status)
          case None =>
            t.updateStatus(tweet)
        }
      } catch {
        case e: Throwable =>
          println(e)
          t.updateStatus(a.tweetString.take(140))
      }
    }
  }

}
