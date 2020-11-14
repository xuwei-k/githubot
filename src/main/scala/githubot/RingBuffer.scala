package githubot

final class RingBuffer[A: reflect.ClassTag](maxSize: Int) extends Iterable[A] {
  private[this] val array = new Array[A](maxSize)
  private[this] var read = 0
  private[this] var write = 0
  private[this] var count_ = 0

  def length = count_
  override def size = count_

  /**
   * Gets the element from the specified index in constant time.
   */
  def apply(i: Int): A = {
    if (i >= count_) throw new IndexOutOfBoundsException(i.toString)
    else array((read + i) % maxSize)
  }

  /**
   * Adds an element, possibly overwriting the oldest elements in the buffer
   * if the buffer is at capacity.
   */
  def +=(elem: A): Unit = {
    array(write) = elem
    write = (write + 1) % maxSize
    if (count_ == maxSize) read = (read + 1) % maxSize
    else count_ += 1
  }

  /**
   * Adds multiple elements, possibly overwriting the oldest elements in
   * the buffer.  If the given iterable contains more elements that this
   * buffer can hold, then only the last maxSize elements will end up in
   * the buffer.
   */
  def ++=(iter: Iterable[A]): Unit = {
    for (elem <- iter) this += elem
  }

  override def iterator: Iterator[A] = new Iterator[A] {
    var idx = 0
    def hasNext = idx != count_
    def next() = {
      val res = apply(idx)
      idx += 1
      res
    }
  }

}
