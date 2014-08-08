import java.net.URL
import githubot._
import scala.concurrent.duration._

new Config{
  val rss = new URL("")
  val interval = 5.minute
  val filter: UserAction => Boolean = Function.const(true)

  val twitter = new TwitterSettings{
    val consumerKey       = ""
    val consumerSecret    = ""
    val accessToken       = ""
    val accessTokenSecret = ""
  }
}

