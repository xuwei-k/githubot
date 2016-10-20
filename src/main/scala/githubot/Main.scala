package githubot

import scala.util.control.Exception.allCatch

object Main{

  def main(args: Array[String]): Unit = {
    val configFile = allCatch.opt(args.head).getOrElse("config.scala")
    val conf = Eval.fromFile[Config](configFile)
    run(conf)
  }

  def run(conf: Config): Unit = {
    import conf._
    val db = new DB[UserActionID](dbSize)
    val client = TweetClient(twitter)
    def tweet(data: Seq[UserAction]): Unit = {
      data.reverseIterator.filter{conf.filter}.foreach{ e =>
        Thread.sleep(tweetInterval.toMillis)
        client.tweet(e)
      }
    }

    def getUserActions() = (xml.XML.load(rss) \ "entry").map{ UserAction.apply }

    val firstData = getUserActions()
    db.insert(firstData.map{_.id}:_*)
    if(firstTweet){
      tweet(firstData)
    }

    @annotation.tailrec
    def _run(): Unit = {
      Thread.sleep(interval.toMillis)
      try{
        val oldIds = db.selectAll
        val newData = getUserActions().filterNot{a => oldIds.contains(a.id)}
        db.insert(newData.map{_.id}:_*)
        tweet(newData)
      }catch{
        case e: Throwable =>
          printDateTime()
          e.printStackTrace()
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
