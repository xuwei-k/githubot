package githubot

final class DB[A: Manifest](size: Int) {

  private[this] val buf = new RingBuffer[A](size)

  def insert(data: List[A]): this.type = {
    buf ++= data.distinct.filterNot { buf.iterator.contains }
    this
  }

  def selectAll: List[A] = buf.toList

}
