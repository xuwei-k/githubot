package githubot

import com.twitter.util.RingBuffer

class DB[A:Manifest](size:Int){

  private val buf = new RingBuffer[A](size)

  def insert(data:A*):this.type = {
    buf ++= data.distinct.filterNot{buf.contains}.toIterable
    this
  }

  def selectAll:List[A] = buf.toList

}

