
package object githubot{

  @inline def allCatchPrintStackTrace(body: => Any){
    try{
      val r = body
    }catch{
      case e => e.printStackTrace
    }
  }

  type UserActionID = String
  type URL = java.net.URL
}
