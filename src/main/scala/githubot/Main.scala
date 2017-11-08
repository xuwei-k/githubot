package githubot

import scala.util.control.Exception.allCatch
import java.io.File

object Main {

  def main(args: Array[String]): Unit = {
    val configFile = allCatch.opt(args.head).getOrElse("config.scala")
    run(new File(configFile))
  }

  def getUserActions(rss: URL): List[UserAction] =
    (scala.xml.XML.load(rss) \ "entry").map { UserAction.apply }.toList

  def run(configFile: File): Unit = {
    val env = Env.fromConfigFile(configFile)
    import env._
    import env.config._
    val firstData = getUserActions(rss)
    println("first data")
    println(firstData)
    db.insert(firstData.map { _.id })
    if (firstTweet) {
      tweet(env, firstData, charCount)
    }
    loop(env)
  }

  def tweet(env: Env, data: Seq[UserAction], charCount: Int): Unit = {
    data.reverseIterator.filter { env.config.filter }.foreach { e =>
      Thread.sleep(env.config.tweetInterval.toMillis)
      val imageOpt = {
        if (env.config.addImage(e)) Some(UserAction.getImageStream(env.config.action2html(e)))
        else None
      }
      env.client.tweet(e, imageOpt, charCount)
    }
  }

  @annotation.tailrec
  def loop(env: Env): Unit = {
    import env._
    import config._
    try {
      Thread.sleep(interval.toMillis)
      val oldIds = db.selectAll
      val newData = getUserActions(rss).filterNot { a =>
        oldIds.contains(a.id)
      }
      env.db.insert(newData.map { _.id })
      tweet(env, newData, charCount)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        mail.foreach { c =>
          allCatchPrintStackTrace {
            println(Mail(e.getMessage, e.getStackTrace.mkString("\n"), c))
          }
        }
    }
    loop(env.reload)
  }
}
