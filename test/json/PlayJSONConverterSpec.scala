package json

import event.Event
import org.specs2.mutable.Specification

class PlayJSONConverterSpec extends Specification {
  "A PlayJSONConverter" should {
    "Convert message JSON into a Message instance" in {
      val msg = new PlayJSONConverter(){}.messageFromJSON("""{"messageType": "t1", "eventId": "1", "payload": "a"}""")
      msg.isDefined must beTrue
      msg.get.messageType must beEqualTo("t1")
      msg.get.eventId must beEqualTo("1")
      msg.get.payload must beEqualTo("a")
    }
    "Convert a sequence of Event into appropriate JSON" in {
      new PlayJSONConverter(){}.eventToJSON(List(
        Event("1", "a"), Event("2", "b")
      )) must beEqualTo("""[{"id":"1","description":"a"},{"id":"2","description":"b"}]""")
    }
  }
}
