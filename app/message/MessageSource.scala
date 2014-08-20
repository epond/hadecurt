package message

import config.AppConfig
import config.AppConfig.ReaderTFuture

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._

trait MessageSource {
  /**
   * produces individual messages as JSON Strings
   */
  def getMessages: ReaderTFuture[AppConfig, Seq[String]]
}

trait FileMessageSource extends MessageSource {
  private def allMessages: String = "" // read from file

  private def breakUp(messages: String): Seq[String] = Nil // split at top level of JSON

  override def getMessages = ReaderTFuture[AppConfig, Seq[String]] { config =>
    Future(breakUp(allMessages))
  }
}

trait DummyMessageSource extends MessageSource {
  override def getMessages = ReaderTFuture[AppConfig, Seq[String]] { config =>
    Future(List(
      """{"messageType": "videoView", "eventId": "1", "payload": "a"}""",
      """{"messageType": "videoView", "eventId": "2", "payload": "b"}"""
    ))
  }
}