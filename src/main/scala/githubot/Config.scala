package githubot

import com.twitter.util.Duration
import com.twitter.conversions.time._

abstract class Config{
  val twitter:TwitterSettings
  val rss:URL
  val interval:Duration
  val dbSize:Int = 100
  val tweetInterval:Duration = 500 millis
  val firstTweet:Boolean = false
  val mail:Option[Mail.Conf] = None
}

abstract class TwitterSettings{
  val consumerKey:String
  val consumerSecret:String
  val accessToken:String
  val accessTokenSecret:String
}

