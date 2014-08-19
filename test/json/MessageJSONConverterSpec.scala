package json

import org.specs2.mutable.Specification

class MessageJSONConverterSpec extends Specification {
  "A MessageJSONConverter" should {
    "Convert message JSON into a Message instance" in {
      val msg = MessageJSONConverter.fromJSON("""{"messageType": "t1", "eventId": "1", "payload": "a"}""")
      msg.isDefined must beTrue
      msg.get.messageType must beEqualTo("t1")
      msg.get.eventId must beEqualTo("1")
      msg.get.payload must beEqualTo("a")
    }
  }
}
