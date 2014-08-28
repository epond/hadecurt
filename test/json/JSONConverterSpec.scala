package json

import event.Event
import org.specs2.mutable.Specification

class JSONConverterSpec extends Specification {
  "A JSONConverter" should {
    "Convert message JSON into a Message instance" in {
      val msg = new JSONConverterImpl(){}.messageFromJSON("""{"messageType": "t1", "eventId": "1", "payload": "a"}""")
      msg.isDefined must beTrue
      msg.get.messageType must beEqualTo("t1")
      msg.get.eventId must beEqualTo("1")
      msg.get.payload must beEqualTo("a")
    }
    "Convert a sequence of Event into appropriate JSON" in {
      new JSONConverterImpl(){}.eventToJSON(List(
        Event("1", "a"), Event("2", "b")
      )) must beEqualTo("""[{"id":"1","description":"a"},{"id":"2","description":"b"}]""")
    }
  }
}
