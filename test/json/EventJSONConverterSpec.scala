package json

import event.Event
import org.specs2.mutable.Specification

class EventJSONConverterSpec extends Specification {
  "An EventJSONConverter" should {
    "Convert a sequence of Event into appropriate JSON" in {
      EventJSONConverter.toJSON(List(
        Event("1", "a"), Event("2", "b")
      )) must beEqualTo("""[{"id":"1","description":"a"},{"id":"2","description":"b"}]""")
    }
  }
}
