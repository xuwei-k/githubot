
package object githubot{

  @inline def allCatchPrintStackTrace(body: => Any){
    try{
      val _ = body
    }catch{
      case e: Throwable =>
        printDateTime()
        e.printStackTrace()
    }
  }

  def printDateTime(): Unit = {
    val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
    println(df.format(new java.util.Date))
  }

  type UserActionID = String
  type URL = java.net.URL
}
