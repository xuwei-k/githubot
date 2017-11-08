package object githubot {

  @inline def allCatchPrintStackTrace(body: => Any) {
    try {
      val _ = body
    } catch {
      case e: Throwable => e.printStackTrace()
    }
  }

  type UserActionID = String
  type URL = java.net.URL
}
