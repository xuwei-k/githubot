package githubot

import java.io.File

final case class Env(
  config: Config,
  db: DB[UserActionID],
  client: TweetClient,
  private val previousConfig: String,
  private val confFile: File
){

  def reload: Env = try{
    val confString = scala.io.Source.fromFile(confFile).mkString
    if(previousConfig != confString){
      val newConfig = Eval[Config](confString)
      println("reload config file")
      println(confString)
      this.copy(config = newConfig, previousConfig = confString, client = TweetClient(newConfig.twitter))
    }else{
      this
    }
  }catch{
    case e: Throwable =>
      e.printStackTrace()
      this
  }
}

object Env {
  def fromConfigFile(file: File): Env = {
    val confString = scala.io.Source.fromFile(file).mkString
    val config = Eval[Config](confString)
    val db = new DB[UserActionID](config.dbSize)
    val client = TweetClient(config.twitter)
    Env(config, db, client, confString, file)
  }
}
