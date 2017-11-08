package githubot

import java.io._
import scala.util.Random

object IO {

  def withTempFile[T](action: File => T): T = {
    val prefix, postfix = Random.alphanumeric.take(5).mkString
    val file = File.createTempFile(prefix, postfix)
    try { action(file) } finally { file.delete() }
  }

  def transfer(in: InputStream, out: OutputStream): Unit = {
    try {
      val buffer = new Array[Byte](1024 * 16)
      @annotation.tailrec
      def read(): Unit = {
        val byteCount = in.read(buffer)
        if (byteCount >= 0) {
          out.write(buffer, 0, byteCount)
          read()
        }
      }
      read()
    } finally { in.close() }
  }

  def file2byteArray(file: File): Array[Byte] = {
    val in = new FileInputStream(file)
    val out = new ByteArrayOutputStream
    transfer(in, out)
    out.toByteArray
  }

  def html2byteArray(html: String): Array[Byte] = {
    val generator = new gui.ava.html.image.generator.HtmlImageGenerator
    generator.loadHtml(html)
    withTempFile { file =>
      generator.saveAsImage(file)
      file2byteArray(file)
    }
  }

}
