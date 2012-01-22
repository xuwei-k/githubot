package githubot

import com.twitter.util.Duration
import java.net.URL

abstract class Config{
  val twitter:TwitterSettings
  val rss:URL
  val interval:Duration
  val dbSize:Int = 100
}

abstract class TwitterSettings{
  val consumerKey:String
  val consumerSecret:String
  val accessToken:String
  val accessTokenSecret:String
}

