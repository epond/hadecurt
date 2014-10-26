package message

import config.AppConfig
import config.AppConfig.ReaderTFuture

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._

trait MessageSource {
  /**
   * produces message chunks
   */
  def getMessages: ReaderTFuture[AppConfig, List[Message]]
}

trait FileMessageSource extends MessageSource {
  private def allMessages: String = "" // TODO read from file

  private def breakUp(messages: String): List[Message] = Nil // TODO split at top level of JSON

  override def getMessages = ReaderTFuture[AppConfig, List[Message]] { config =>
    Future(breakUp(allMessages))
  }
}

trait HardcodedMessageSource extends MessageSource {
  override def getMessages = ReaderTFuture[AppConfig, List[Message]] { config =>
    val messages = (1 to config.messageChunkSize).toList.map{ index =>
      Message("videoView", index.toString, "payload" + index)
    }
    Future(messages)
  }
}