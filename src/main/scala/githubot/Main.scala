package githubot

import scala.util.control.Exception.allCatch
import com.twitter.util.Eval
import java.io.File

object Main{

  def main(args:Array[String]){
    val configFile = new File(
      allCatch.opt(args.head).getOrElse("config.scala")
    )
    val conf = Eval[Config](configFile)
    run(conf)
  }

  def run(conf:Config){
    import conf._
    val db = new DB[UserActionID](dbSize)
    val client = TweetClient(twitter)
    def tweet(data:Seq[UserAction]){
      data.reverseIterator.filter{conf.filter}.foreach{ e =>
        Thread.sleep(tweetInterval.inMillis)
        client.tweet(e)
      }
    }

    def getUserActions = (xml.XML.load(rss) \ "entry").map{ UserAction.apply }

    val firstData = getUserActions
    db.insert(firstData.map{_.id}:_*)
    if(firstTweet){
      tweet(firstData)
    }

    @annotation.tailrec
    def _run(){
      Thread.sleep(interval.inMillis)
      try{
        val oldIds = db.selectAll
        val newData = getUserActions.filterNot{a => oldIds.contains(a.id)}
        db.insert(newData.map{_.id}:_*)
        tweet(newData)
      }catch{
        case e =>
          e.printStackTrace
          mail.foreach{c =>
            allCatchPrintStackTrace{
              println(Mail(e.getMessage,e.getStackTrace.mkString("\n"),c))
            }
          }
      }
      _run()
    }

    _run()
  }
}
