package message

import config.AppConfig
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._

class MessageSourceSpec extends Specification {
  object TestMessageSource extends HardcodedMessageSource

  "A HardcodedMessageSource" should {
    "given messageChunkSize of 1 when getMessages is called then return 1 message" in {
      val result = Await.result(TestMessageSource.getMessages(AppConfig(1)), 0 nanos)
      result.size must beEqualTo(1)
    }

    "given messageChunkSize of 3 when getMessages is called then return 3 messages" in {
      val result = Await.result(TestMessageSource.getMessages(AppConfig(3)), 0 nanos)
      result.size must beEqualTo(3)
    }
  }
}
