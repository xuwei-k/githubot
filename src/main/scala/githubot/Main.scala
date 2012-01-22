package githubot

import scala.util.control.Exception.allCatch
import com.twitter.util.Eval
import java.io.File

object Main{

  def main(args:Array[String]){
    val configFile = new File(
      allCatch.opt(args.head).getOrElse("config")
    )
    val conf = Eval[Config](configFile)
    run(conf)
  }

  def run(conf:Config){
    val db = new DB[UserActionID](conf.dbSize)
    val client = TweetClient(conf.twitter)
    while(true){
      allCatchPrintStackTrace{
        val o = (xml.XML.load(conf.rss) \ "entry").map{ UserAction.apply }
        val oldIds = db.selectAll
        val newData = o.filterNot{a => oldIds.contains(a.id)}
        db.insert(newData.map{_.id}:_*)
        newData.reverse.foreach{ Thread.sleep(500) ; client.tweet }
        Thread.sleep(conf.interval.inMillis)
      }
    }
  }

}
