package githubot

import scala.concurrent.duration._

abstract class Config{
  val twitter: TwitterSettings
  val rss: URL
  val interval: Duration
  val dbSize: Int = 100
  val filter: UserAction => Boolean
  val tweetInterval: Duration = 500 millis
  val firstTweet: Boolean = false
  val mail: Option[Mail.Conf] = None
  val uploadImage: Option[ImageSetting]
}

abstract class ImageSetting{
  val size: Int
}

abstract class TwitterSettings{
  val consumerKey: String
  val consumerSecret: String
  val accessToken: String
  val accessTokenSecret: String
}

